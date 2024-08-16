package daniel.bertoldi.geographyquiz.presentation.viewmodel

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.domain.usecase.GetFlagGameOptionsUseCase
import daniel.bertoldi.geographyquiz.presentation.model.CountryFlagUi
import daniel.bertoldi.geographyquiz.presentation.model.GameMode
import daniel.bertoldi.geographyquiz.presentation.model.GameMode.Companion.toGameMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class FlagGameViewModel @Inject constructor(
    private val getFlagGameOptionsUseCase: GetFlagGameOptionsUseCase,
) : ViewModel() {
    private val allAvailableCountries = mutableListOf<CountryFlagUi>()
    private val countriesYetToBeDrawn = mutableListOf<CountryFlagUi>()
    private val _gameState = MutableStateFlow(GameState())
    val gameState = _gameState.asStateFlow()

    private lateinit var chosenRegion: String
    private lateinit var chosenSubRegion: String
    private lateinit var chosenGameMode: GameMode

    fun init(bundle: Bundle?) {
        chosenRegion = bundle?.getString("region").orEmpty()
        chosenSubRegion = bundle?.getString("subregion").orEmpty()
        chosenGameMode = bundle?.getString("gamemode").orEmpty().toGameMode()

        startFlagGame()
    }

    fun nextRound() {
        val currentGameState = _gameState.value

        if (currentGameState.roundState.currentRound == allAvailableCountries.size) {
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

    fun retryCurrentGame() {
        countriesYetToBeDrawn.addAll(0, allAvailableCountries)
        _gameState.value = _gameState.value.copy(
            score = 0,
            roundState = _gameState.value.roundState.copy(
                currentRound = 1,
                hits = 0,
                misses = 0,
            ),
        )

        val correctAnswerIdx = drawFlagOptions()
        countriesYetToBeDrawn.removeAt(correctAnswerIdx)
    }

    fun saveDuration(duration: Duration) {
        _gameState.value = _gameState.value.copy(
            duration = duration,
        )
    }

    private fun startFlagGame() {
        viewModelScope.launch(Dispatchers.IO) {
            getFlagGameOptionsUseCase(chosenRegion, chosenSubRegion).collect {
                allAvailableCountries.addAll(it)
                countriesYetToBeDrawn.addAll(it)
                _gameState.value = _gameState.value.copy(
                    gameMode = chosenGameMode,
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
}

data class GameState(
    val step: GameStep = GameStep.CHOOSING_OPTION,
    val gameMode: GameMode = GameMode.Casual(),
    val correctCountryCode: String = "",
    val chosenCountryCode: String = "",
    val availableOptions: List<CountryFlagUi> = emptyList(),
    val score: Int = 0,
    val duration: Duration = 0.seconds,
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
    val accuracy: Double
        get() = hits.toDouble() / totalFlags * 100

    fun getGameRank(): GameRank {
        if (hits + misses == 0) return GameRank.NONE

        return when {
            accuracy == 100.0 -> GameRank.FLAWLESS
            accuracy >= 70.0 -> GameRank.EXCELLENT
            accuracy >= 50.0 -> GameRank.GOOD
            accuracy >= 30.0 -> GameRank.AVERAGE
            accuracy >= 10.0 -> GameRank.POOR
            accuracy >= 0.0 -> GameRank.VERY_POOR
            else -> GameRank.FAIL
        }
    }
}

enum class GameStep {
    CHOOSING_OPTION,
    OPTION_SELECTED,
    END_GAME,
}

enum class GameRank(
    @StringRes val title: Int,
    @StringRes val description: Int,
    @DrawableRes val image: Int,
) {
    FLAWLESS(
        title = R.string.game_rank_flawless_title,
        description = R.string.game_rank_flawless_description,
        image = R.drawable.rank_flawless,
    ),
    EXCELLENT(
        title = R.string.game_rank_excellent_title,
        description = R.string.game_rank_excellent_description,
        image = R.drawable.rank_excellent,
    ),
    GOOD(
        title = R.string.game_rank_good_title,
        description = R.string.game_rank_good_description,
        image = R.drawable.rank_good,
    ),
    AVERAGE(
        title = R.string.game_rank_average_title,
        description = R.string.game_rank_average_description,
        image = R.drawable.rank_average,
    ),
    POOR(
        title = R.string.game_rank_poor_title,
        description = R.string.game_rank_poor_description,
        image = R.drawable.rank_poor,
    ),
    VERY_POOR(
        title = R.string.game_rank_very_poor_title,
        description = R.string.game_rank_very_poor_description,
        image = R.drawable.rank_very_poor,
    ),
    FAIL(
        title = R.string.game_rank_fail_title,
        description = R.string.game_rank_fail_description,
        image = R.drawable.rank_fail,
    ),
    NONE(
        title = R.string.game_rank_none_title,
        description = R.string.game_rank_none_description,
        image = R.drawable.broken_earth,
    )
}