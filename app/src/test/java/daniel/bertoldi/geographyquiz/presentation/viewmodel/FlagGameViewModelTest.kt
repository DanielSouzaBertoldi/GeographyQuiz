package daniel.bertoldi.geographyquiz.presentation.viewmodel

import daniel.bertoldi.geographyquiz.domain.usecase.FetchUserHighScoresForGameUseCase
import daniel.bertoldi.geographyquiz.domain.usecase.GetFlagGameOptionsUseCase
import daniel.bertoldi.geographyquiz.domain.usecase.SaveUserScoreUseCase
import daniel.bertoldi.geographyquiz.factory.CountryUiModelFactory
import daniel.bertoldi.geographyquiz.presentation.navigation.ScreenRoutes
import daniel.bertoldi.utilities.test.utils.randomInt
import daniel.bertoldi.utilities.test.utils.randomString
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.Ignore
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class FlagGameViewModelTest {
    private val getFlagGameOptionsUseCase = mockk<GetFlagGameOptionsUseCase>()
    private val saveUserScoreUseCase = mockk<SaveUserScoreUseCase>()
    private val fetchUserHighScoresForGameUseCase = mockk<FetchUserHighScoresForGameUseCase>()
    private val viewModel = FlagGameViewModel(
        getFlagGameOptionsUseCase = getFlagGameOptionsUseCase,
        saveUserScore = saveUserScoreUseCase,
        fetchUserHighScoresForGame = fetchUserHighScoresForGameUseCase,
    )

    @Test
    fun onInit_verifyGetFlagGameOptionsUseCaseCalled() = runTest {
        coEvery {
            getFlagGameOptionsUseCase(any(), any())
        } returns flowOf(CountryUiModelFactory.makeList())

        viewModel.init(
            ScreenRoutes.FlagGame(
                region = "americas",
                subRegion = "south america",
                gameMode = randomString(),
            )
        )

        coVerify(exactly = 1) {
            getFlagGameOptionsUseCase("americas", "south america")
        }
    }

    @Test
    fun onInit_withRandomNumberOfCountries_assertGameStateTotalFlagsEqualsToRandomNumber_noTurbine() =
        runTest {
            val numberOfCountries = randomInt()
            coEvery {
                getFlagGameOptionsUseCase(any(), any())
            } returns flowOf(CountryUiModelFactory.makeList(listSize = numberOfCountries))

            // TODO: why don't this work like in the docs?
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.init(
                    ScreenRoutes.FlagGame(
                        region = "americas",
                        subRegion = "south america",
                        gameMode = randomString(),
                    )
                )
                val gameState = viewModel.gameState.first()
                assertEquals(numberOfCountries, gameState.roundState.totalFlags)
            }
        }

    @Test
    fun onInit_assertGameStateStepIsChoosingOption() = runTest {
        coEvery {
            getFlagGameOptionsUseCase(any(), any())
        } returns flowOf(CountryUiModelFactory.makeList())

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.init(
                ScreenRoutes.FlagGame(
                    region = "americas",
                    subRegion = "south america",
                    gameMode = randomString(),
                )
            )
            val gameState = viewModel.gameState.first()
            assertEquals(GameStep.CHOOSING_OPTION, gameState.step)
        }
    }

    @Test
    fun onInit_withMoreThanFourCountries_assertGameStateAvailableOptionsListWithFourOptions() =
        runTest {
            coEvery {
                getFlagGameOptionsUseCase(any(), any())
            } returns flowOf(CountryUiModelFactory.makeList(listSize = 10))

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.init(
                    ScreenRoutes.FlagGame(
                        region = "americas",
                        subRegion = "south america",
                        gameMode = randomString(),
                    )
                )
                val gameState = viewModel.gameState.first()
                assertEquals(4, gameState.availableOptions.size)
            }
        }

    @Ignore // TODO: currently the code can't handle games with less than 4 available countries. Fix that.
    @Test
    fun onInit_withFewerThanFourCountries_assertGameStateAvailableOptionsListWithFourOptions() =
        runTest {
            coEvery {
                getFlagGameOptionsUseCase(any(), any())
            } returns flowOf(CountryUiModelFactory.makeList(listSize = 3))

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.init(
                    ScreenRoutes.FlagGame(
                        region = "americas",
                        subRegion = "south america",
                        gameMode = randomString(),
                    )
                )
                val gameState = viewModel.gameState.first()
                assertEquals(4, gameState.availableOptions.size)
            }
        }

    @Ignore // TODO: this doesn't work either.
    @Test
    fun onNextRound_withAllRoundsConcluded_assertGameStepIsEndGame() = runTest {
        coEvery {
            getFlagGameOptionsUseCase(any(), any())
        } returns flowOf(CountryUiModelFactory.makeList(listSize = 10))

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {

            viewModel.init(
                ScreenRoutes.FlagGame(
                    region = "americas",
                    subRegion = "south america",
                    gameMode = randomString(),
                )
            )
            viewModel.nextRound()

            val gameState = viewModel.gameState.first()
            assertEquals(GameStep.END_GAME, gameState.step)
        }
    }

    @Ignore // TODO: this doesn't work either.
    @Test
    fun onOptionClick_withCorrectAnswer_assertNumberOfHitsIncremented() = runTest {
        coEvery {
            getFlagGameOptionsUseCase(any(), any())
        } returns flowOf(CountryUiModelFactory.makeList(listSize = 10))
        every { Random.nextInt(any()) } returns 1

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {

            viewModel.init(
                ScreenRoutes.FlagGame(
                    region = "americas",
                    subRegion = "south america",
                    gameMode = randomString(),
                )
            )
            viewModel.optionClick("abc")

            val gameState = viewModel.gameState.first()
            assertEquals(1, gameState.roundState.hits)
        }
    }
}