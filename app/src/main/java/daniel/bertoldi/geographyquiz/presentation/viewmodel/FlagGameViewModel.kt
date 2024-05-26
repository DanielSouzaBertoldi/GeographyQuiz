package daniel.bertoldi.geographyquiz.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.bertoldi.database.CountryEntity
import daniel.bertoldi.database.CountriesDatabaseInterface
import daniel.bertoldi.geographyquiz.domain.usecase.GetFlagGameOptionsUseCase
import daniel.bertoldi.geographyquiz.presentation.model.CountryFlagUi
import daniel.bertoldi.geographyquiz.presentation.model.GameMode.Companion.toGameMode
import daniel.bertoldi.geographyquiz.presentation.model.Region.Companion.toRegion
import daniel.bertoldi.geographyquiz.presentation.model.SubRegion.Companion.toSubRegion
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
    private val countries = MutableStateFlow<List<CountryFlagUi>>(emptyList())
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState = _gameState.asStateFlow()

    fun startFlagGame(bundle: Bundle?) {
        val chosenRegion = bundle?.getString("region").orEmpty()
        val chosenSubRegion = bundle?.getString("subregion").orEmpty()
        val chosenGameMode = bundle?.getString("gamemode").orEmpty().toGameMode()

        viewModelScope.launch(Dispatchers.IO) {
            getFlagGameOptionsUseCase(chosenRegion, chosenSubRegion).collect {
                countries.value = it

                val drawnCountries = countries.value.shuffled().take(5).toMutableList()

                _gameState.value = GameState(
                    availableOptions = drawFlagOptions(drawnCountries),
                )
            }
        }
    }

    private fun drawFlagOptions(countries: MutableList<CountryFlagUi>): List<FlagOption> {
        val flagOptions = mutableListOf<FlagOption>()
        val drawnAnswer = countries.random()

        for (i in 1..5) {
            val randomNumber = Random.nextInt(from = 0, until = countries.size)
            val randomPosition = randomNumber % countries.size
            val randomCountry = countries[randomPosition]

            flagOptions.add(
                FlagOption(
                    isTheCorrectAnswer = randomCountry.countryCode == drawnAnswer.countryCode,
                    countryData = randomCountry,
                )
            )
            countries.removeAt(randomPosition)
        }
        return flagOptions
    }

    fun drawAgain() {
        val filteredCountries = countries.value
            .filterNot { it.countryCode == gameState.value?.availableOptions?.find { it.isTheCorrectAnswer }?.countryData?.countryCode }
        val drawnCountries = filteredCountries.shuffled().take(5).toMutableList()

        _gameState.value = _gameState.value?.copy(
            step = GameStep.CHOOSING_OPTION,
            availableOptions = drawFlagOptions(drawnCountries),
        )
    }

    fun optionClick(clickedOption: CountryFlagUi) {
        var pointsToAdd = _gameState.value?.score ?: 0
        if (clickedOption.countryCode == gameState.value?.availableOptions?.find { it.isTheCorrectAnswer }?.countryData?.countryCode) {
            pointsToAdd += 20
        } else {
            pointsToAdd -= 20
        }

        _gameState.value = _gameState.value?.copy(
            step = GameStep.OPTION_SELECTED,
            clickedOption = clickedOption,
            score = pointsToAdd,
        )
    }
}

data class GameState(
    val step: GameStep = GameStep.CHOOSING_OPTION,
    val availableOptions: List<FlagOption>,
    val clickedOption: CountryFlagUi? = null,
    val score: Int = 0,
)

data class FlagOption(
    val isTheCorrectAnswer: Boolean = false,
    val countryData: CountryFlagUi,
)

enum class GameStep {
    CHOOSING_OPTION,
    OPTION_SELECTED,
}