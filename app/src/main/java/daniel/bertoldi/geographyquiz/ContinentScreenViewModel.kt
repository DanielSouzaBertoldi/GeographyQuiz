package daniel.bertoldi.geographyquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.bertoldi.database.CountryEntity
import daniel.bertoldi.database.DatabaseInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


enum class Continent(val simpleName: String) {
    NORTH_AMERICA("North America"),
    SOUTH_AMERICA("South America"),
    EUROPE("Europe"),
    AFRICA("Africa"),
    ASIA("Asia"),
    OCEANIA("Oceania")
}

sealed class GameScreenState {
    data object SelectContinent : GameScreenState()
    data object StartGame : GameScreenState()
}

@HiltViewModel
class ContinentScreenViewModel @Inject constructor(
    private val databaseInterface: DatabaseInterface,
) : ViewModel() {
    private val countries = MutableStateFlow<List<CountryEntity>>(emptyList())
    private val _gameScreenState = MutableStateFlow<GameScreenState>(GameScreenState.SelectContinent)
    val gameScreenState: StateFlow<GameScreenState> = _gameScreenState.asStateFlow()
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState = _gameState.asStateFlow()

    fun startFlagGame(continent: Continent) {
        viewModelScope.launch(Dispatchers.IO) {
            countries.value = databaseInterface.fetchCountriesInContinent(continent.simpleName)
            val drawnCountries = countries.value.shuffled().take(5).toMutableList()

            _gameState.value = GameState(
                availableOptions = drawFlagOptions(drawnCountries),
                chosenContinent = continent,
            )
            _gameScreenState.value = GameScreenState.StartGame
        }
    }

    private fun drawFlagOptions(countries: MutableList<CountryEntity>): List<FlagOption> {
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
        _gameScreenState.value = GameScreenState.StartGame
    }

    fun optionClick(clickedOption: CountryEntity) {
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
    val chosenContinent: Continent,
    val clickedOption: CountryEntity? = null,
    val score: Int = 0,
)

data class FlagOption(
    val isTheCorrectAnswer: Boolean = false,
    val countryData: CountryEntity,
)

enum class GameStep {
    CHOOSING_OPTION,
    OPTION_SELECTED,
}