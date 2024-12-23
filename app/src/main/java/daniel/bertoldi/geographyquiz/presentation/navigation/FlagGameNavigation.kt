package daniel.bertoldi.geographyquiz.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import daniel.bertoldi.geographyquiz.presentation.ui.components.ChooseAreaComponent
import daniel.bertoldi.geographyquiz.presentation.ui.components.ErrorComponent
import daniel.bertoldi.geographyquiz.presentation.ui.components.FlagGameComponent
import daniel.bertoldi.geographyquiz.presentation.ui.components.HOME_SCREEN_TEST_TAG
import daniel.bertoldi.geographyquiz.presentation.ui.components.HomeComponent
import daniel.bertoldi.geographyquiz.presentation.ui.components.LoadingComponent
import daniel.bertoldi.geographyquiz.presentation.ui.components.SelectGameMode
import daniel.bertoldi.geographyquiz.presentation.ui.components.SelectRegionComponent
import daniel.bertoldi.geographyquiz.presentation.viewmodel.AreaViewModel
import daniel.bertoldi.geographyquiz.presentation.viewmodel.FlagGameViewModel
import daniel.bertoldi.geographyquiz.presentation.viewmodel.GameModeViewModel
import daniel.bertoldi.geographyquiz.presentation.viewmodel.MainActivityViewModel
import daniel.bertoldi.geographyquiz.presentation.viewmodel.MainScreenState

fun NavGraphBuilder.flagGameDestinations(
    onNavigateToRegionSelection: () -> Unit,
    onNavigateToAreaSelection: (region: String) -> Unit,
    onNavigateToGameModeSelection: (region: String, area: String) -> Unit,
    onNavigateToFlagGame: (region: String, area: String, gameMode: String) -> Unit,
    onPopBackStackToHome: () -> Unit,
    onPopBackStackToRegionSelection: () -> Unit,
) {
    composable<ScreenRoutes.Home> {
        val viewModel = hiltViewModel<MainActivityViewModel>()
        val screenState = viewModel.mainScreenState.collectAsStateWithLifecycle()

        Column(modifier = Modifier.fillMaxSize().testTag(HOME_SCREEN_TEST_TAG)) {
            when (screenState.value) {
                is MainScreenState.Loading -> LoadingComponent()
                is MainScreenState.Success -> HomeComponent(
                    navigateToGameScreen = { onNavigateToRegionSelection() },
                )

                is MainScreenState.Failed -> ErrorComponent()
            }
        }
    }
    composable<ScreenRoutes.RegionSelection> {
        SelectRegionComponent(nextStep = { onNavigateToAreaSelection(it.simpleName) })
    }
    composable<ScreenRoutes.AreaSelection> {
        val viewModel = hiltViewModel<AreaViewModel>()
        val region = it.toRoute<ScreenRoutes.AreaSelection>().region

        LaunchedEffect(key1 = region) {
            viewModel.fetchSubRegions(region)
        }

        ChooseAreaComponent(
            screenState = viewModel.screenState.collectAsStateWithLifecycle().value,
            nextStep = { subRegion ->
                onNavigateToGameModeSelection(region, subRegion)
            },
        )
    }
    composable<ScreenRoutes.GameModeSelection> { backStackEntry ->
        val viewModel = hiltViewModel<GameModeViewModel>()

        LaunchedEffect(key1 = Unit) {
            viewModel.init(backStackEntry.toRoute())
        }

        SelectGameMode(
            screenState = viewModel.screenState.collectAsStateWithLifecycle().value,
            onGameModeClick = { gameMode, region, subRegion ->
                viewModel.confirmSelection(gameMode, region, subRegion)
            },
            onPlayGameClick = { region, subRegion, gameMode ->
                onNavigateToFlagGame(region, subRegion, gameMode.name)
            },
            onUndoChoicesClick = { onNavigateToRegionSelection() },
        )
    }
    composable<ScreenRoutes.FlagGame> { backStackEntry ->
        val viewModel = hiltViewModel<FlagGameViewModel>()

        LaunchedEffect(key1 = Unit) { viewModel.init(backStackEntry.toRoute()) }

        FlagGameComponent(
            gameState = viewModel.gameState.collectAsStateWithLifecycle().value,
            optionClick = { countryCode -> viewModel.optionClick(countryCode) },
            nextRound = { viewModel.nextRound() },
            giveUp = { onPopBackStackToHome() },
            onPlayAgain = { onPopBackStackToRegionSelection() },
            onRetry = { viewModel.retryCurrentGame() },
            onGameEnd = { viewModel.endGame(it) },
        )
    }
}

fun NavController.navigateToRegionSelection() {
    navigate(route = ScreenRoutes.RegionSelection)
}

fun NavController.navigateToAreaSelection(chosenRegion: String) {
    navigate(
        route = ScreenRoutes.AreaSelection(region = chosenRegion),
    )
}

fun NavController.navigateToGameModeSelection(chosenRegion: String, chosenArea: String) {
    navigate(
        route = ScreenRoutes.GameModeSelection(region = chosenRegion, area = chosenArea),
    )
}

fun NavController.navigateToFlagGame(
    chosenRegion: String,
    chosenArea: String,
    chosenGameMode: String,
) {
    navigate(
        route = ScreenRoutes.FlagGame(
            region = chosenRegion,
            subRegion = chosenArea,
            gameMode = chosenGameMode,
        ),
    )
}

fun NavController.popBackStackToHome() {
    popBackStack(ScreenRoutes.Home, inclusive = false)
}

fun NavController.popBackStackToRegionSelection() {
    popBackStack(ScreenRoutes.RegionSelection, inclusive = false)
}