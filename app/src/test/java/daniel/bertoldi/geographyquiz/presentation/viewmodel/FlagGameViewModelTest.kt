package daniel.bertoldi.geographyquiz.presentation.viewmodel

import android.os.Bundle
import app.cash.turbine.test
import daniel.bertoldi.geographyquiz.domain.usecase.GetFlagGameOptionsUseCase
import daniel.bertoldi.geographyquiz.factory.CountryUiModelFactory
import daniel.bertoldi.test.utils.randomInt
import daniel.bertoldi.test.utils.randomString
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
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
    private val random = spyk<Random>()
    private val bundle = mockk<Bundle>()
    private val getFlagGameOptionsUseCase = mockk<GetFlagGameOptionsUseCase>()
    private val viewModel = FlagGameViewModel(getFlagGameOptionsUseCase)

    @Test
    fun onInit_verifyGetFlagGameOptionsUseCaseCalled() = runTest {
        prepareBundleMock()
        coEvery {
            getFlagGameOptionsUseCase(any(), any())
        } returns flowOf(CountryUiModelFactory.makeList())

        viewModel.init(bundle)

        coVerify(exactly = 1) {
            getFlagGameOptionsUseCase("americas", "south america")
        }
    }

    @Test
    fun onInit_withRandomNumberOfCountries_assertGameStateTotalFlagsEqualsToRandomNumber_noTurbine() =
        runTest {
            prepareBundleMock()
            val numberOfCountries = randomInt()
            coEvery {
                getFlagGameOptionsUseCase(any(), any())
            } returns flowOf(CountryUiModelFactory.makeList(listSize = numberOfCountries))

            // TODO: why don't this work like in the docs?
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.init(bundle)
                val gameState = viewModel.gameState.first()
                assertEquals(numberOfCountries, gameState.roundState.totalFlags)
            }
        }

    @Test
    fun onInit_assertGameStateStepIsChoosingOption() = runTest {
        prepareBundleMock()
        coEvery {
            getFlagGameOptionsUseCase(any(), any())
        } returns flowOf(CountryUiModelFactory.makeList())

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.init(bundle)
            val gameState = viewModel.gameState.first()
            assertEquals(GameStep.CHOOSING_OPTION, gameState.step)
        }
    }

    @Test
    fun onInit_withMoreThanFourCountries_assertGameStateAvailableOptionsListWithFourOptions() =
        runTest {
            prepareBundleMock()
            coEvery {
                getFlagGameOptionsUseCase(any(), any())
            } returns flowOf(CountryUiModelFactory.makeList(listSize = 10))

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.init(bundle)
                val gameState = viewModel.gameState.first()
                assertEquals(4, gameState.availableOptions.size)
            }
        }

    @Ignore // TODO: currently the code can't handle games with less than 4 available countries. Fix that.
    @Test
    fun onInit_withFewerThanFourCountries_assertGameStateAvailableOptionsListWithFourOptions() =
        runTest {
            prepareBundleMock()
            coEvery {
                getFlagGameOptionsUseCase(any(), any())
            } returns flowOf(CountryUiModelFactory.makeList(listSize = 3))

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.init(bundle)
                val gameState = viewModel.gameState.first()
                assertEquals(4, gameState.availableOptions.size)
            }
        }

    @Ignore // TODO: this doesn't work either.
    @Test
    fun onNextRound_withAllRoundsConcluded_assertGameStepIsEndGame() = runTest {
        prepareBundleMock()
        coEvery {
            getFlagGameOptionsUseCase(any(), any())
        } returns flowOf(CountryUiModelFactory.makeList(listSize = 10))

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {

            viewModel.init(bundle)
            viewModel.nextRound()

            val gameState = viewModel.gameState.first()
            assertEquals(GameStep.END_GAME, gameState.step)
        }
    }

    @Ignore // TODO: this doesn't work either.
    @Test
    fun onOptionClick_withCorrectAnswer_assertNumberOfHitsIncremented() = runTest {
        prepareBundleMock()
        coEvery {
            getFlagGameOptionsUseCase(any(), any())
        } returns flowOf(CountryUiModelFactory.makeList(listSize = 10))
        every { Random.nextInt(any()) } returns 1

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {

            viewModel.init(bundle)
            viewModel.optionClick("abc")

            val gameState = viewModel.gameState.first()
            assertEquals(1, gameState.roundState.hits)
        }
    }

    private fun prepareBundleMock() {
        every { bundle.getString("region") } returns "americas"
        every { bundle.getString("subregion") } returns "south america"
        every { bundle.getString("gamemode") } returns randomString()
    }
}