package daniel.bertoldi.geographyquiz.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.bertoldi.geographyquiz.domain.usecase.GetFlagGameOptionsUseCase
import daniel.bertoldi.geographyquiz.presentation.model.CountryFlagUi
import daniel.bertoldi.geographyquiz.presentation.model.GameMode.Companion.toGameMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class FlagGameViewModel @Inject constructor(
    private val getFlagGameOptionsUseCase: GetFlagGameOptionsUseCase,
) : ViewModel() {
    private val allAvailableCountries = mutableListOf<CountryFlagUi>()
    private val countriesYetToBeDrawn = mutableListOf<CountryFlagUi>()
    private val _gameState = MutableStateFlow(GameState())
    val gameState = _gameState.asStateFlow()

    fun startFlagGame(bundle: Bundle?) {
        val chosenRegion = bundle?.getString("region").orEmpty()
        val chosenSubRegion = bundle?.getString("subregion").orEmpty()
        val chosenGameMode = bundle?.getString("gamemode").orEmpty().toGameMode()

        viewModelScope.launch(Dispatchers.IO) {
            getFlagGameOptionsUseCase(chosenRegion, chosenSubRegion).collect {
                allAvailableCountries.addAll(it)
                countriesYetToBeDrawn.addAll(it)
                _gameState.value = _gameState.value.copy(limit = it.size)

                val correctAnswerIdx = drawFlagOptions()
                countriesYetToBeDrawn.removeAt(correctAnswerIdx)
            }
        }
    }

    private fun drawFlagOptions(): Int {
        val validCountries = countriesYetToBeDrawn.toMutableList()
        val countryOptions = mutableListOf<CountryFlagUi>()
        val correctAnswerIdx = Random.nextInt(validCountries.size)
        val correctCountry = validCountries[correctAnswerIdx]
        validCountries.removeAt(correctAnswerIdx)

        repeat(3) {
            val randomCountry = if (validCountries.size == 0) {
                allAvailableCountries.filter { it != correctCountry }.random()
            } else {
                val idx = Random.nextInt(validCountries.size)
                validCountries[idx]
            }
            validCountries.remove(randomCountry)

            countryOptions.add(randomCountry)
        }
        countryOptions.add(correctCountry)

        _gameState.value = _gameState.value.copy(
            step = GameStep.CHOOSING_OPTION,
            availableOptions = countryOptions.shuffled(),
            correctCountryCode = correctCountry.countryCode,
        )
        return correctAnswerIdx
    }

    fun drawAgain() {
        if (_gameState.value.round == allAvailableCountries.size){
            _gameState.value = _gameState.value.copy(
                step = GameStep.END_GAME,
            )
        } else {
            _gameState.value = _gameState.value.copy(
                round = _gameState.value.round.inc()
            )
            val correctAnswerIdx = drawFlagOptions()
            countriesYetToBeDrawn.removeAt(correctAnswerIdx)
        }
    }

    fun optionClick(countryCodeSelected: String) {
        var pointsToAdd = _gameState.value.score
        val isOptionCorrect = countryCodeSelected == _gameState.value.correctCountryCode
        if (isOptionCorrect) pointsToAdd += 20 else pointsToAdd -= 20

        _gameState.value = _gameState.value.copy(
            step = GameStep.OPTION_SELECTED,
            chosenCountryCode = countryCodeSelected,
            score = pointsToAdd,
        )
    }
}

data class GameState(
    val step: GameStep = GameStep.CHOOSING_OPTION,
    val correctCountryCode: String = "",
    val chosenCountryCode: String = "",
    val availableOptions: List<CountryFlagUi> = emptyList(),
    val score: Int = 0,
    val round: Int = 1,
    val limit: Int = 1,
) {
    fun userHasChosen() = this.step == GameStep.OPTION_SELECTED
    fun isCorrectAnswer(countryCode: String) = correctCountryCode == countryCode
    fun wrongUserOption(countryCode: String) = chosenCountryCode == countryCode
}

enum class GameStep {
    CHOOSING_OPTION,
    OPTION_SELECTED,
    END_GAME,
}