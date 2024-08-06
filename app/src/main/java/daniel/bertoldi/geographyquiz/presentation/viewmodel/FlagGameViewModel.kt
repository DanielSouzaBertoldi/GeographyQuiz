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
                _gameState.value = _gameState.value.copy(
                    roundState = _gameState.value.roundState.copy(totalFlags = it.size),
                )

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
        countryOptions.add(correctCountry)

        repeat(3) {
            val randomCountry = if (validCountries.size == 0) {
                allAvailableCountries.filterNot { it in countryOptions }.random()
            } else {
                val idx = Random.nextInt(validCountries.size)
                validCountries[idx]
            }

            validCountries.remove(randomCountry)
            countryOptions.add(randomCountry)
        }

        _gameState.value = _gameState.value.copy(
            step = GameStep.CHOOSING_OPTION,
            availableOptions = countryOptions.shuffled(),
            correctCountryCode = correctCountry.countryCode,
        )
        return correctAnswerIdx
    }

    fun nextRound() {
        val currentGameState = _gameState.value

        if (currentGameState.roundState.currentRound == allAvailableCountries.size){
            _gameState.value = currentGameState.copy(step = GameStep.END_GAME)
        } else {
            _gameState.value = currentGameState.copy(
                roundState = currentGameState.roundState.copy(
                    currentRound = currentGameState.roundState.currentRound.inc(),
                )
            )
            val correctAnswerIdx = drawFlagOptions()
            countriesYetToBeDrawn.removeAt(correctAnswerIdx)
        }
    }

    fun optionClick(countryCodeSelected: String) {
        val currentGameState = _gameState.value
        var pointsToAdd = currentGameState.score
        val isOptionCorrect = countryCodeSelected == currentGameState.correctCountryCode

        if (isOptionCorrect) {
            pointsToAdd += 20
            _gameState.value = currentGameState.copy(
                roundState = currentGameState.roundState.copy(
                    hits = currentGameState.roundState.hits.inc(),
                )
            )
        } else {
            pointsToAdd -= 20
            _gameState.value = currentGameState.copy(
                roundState = currentGameState.roundState.copy(
                    misses = currentGameState.roundState.misses.inc(),
                )
            )
        }

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
    val roundState: RoundState = RoundState(),
) {
    fun userHasChosen() = this.step == GameStep.OPTION_SELECTED
    fun isCorrectAnswer(countryCode: String) = correctCountryCode == countryCode
    fun wrongUserOption(countryCode: String) = chosenCountryCode == countryCode
}

data class RoundState(
    val currentRound: Int = 1,
    val totalFlags: Int = 1,
    val hits: Int = 0,
    val misses: Int = 0,
) {
    fun getGameRank(): GameRank {
        if (hits + misses == 0) return GameRank.NONE

        val hitPercentage = (hits.toDouble() / misses.toDouble()) * 100
        return when {
            hitPercentage == 100.0 -> GameRank.FLAWLESS
            hitPercentage >= 70.0 -> GameRank.EXCELLENT
            hitPercentage >= 50.0 -> GameRank.GOOD
            hitPercentage >= 30.0 -> GameRank.AVERAGE
            hitPercentage >= 10.0 -> GameRank.POOR
            hitPercentage >= 0.0 -> GameRank.VERY_POOR
            else -> GameRank.FAIL
        }
    }
}

enum class GameStep {
    CHOOSING_OPTION,
    OPTION_SELECTED,
    END_GAME,
}

enum class GameRank {
    FLAWLESS,
    EXCELLENT,
    GOOD,
    AVERAGE,
    POOR,
    VERY_POOR,
    FAIL,
    NONE
}