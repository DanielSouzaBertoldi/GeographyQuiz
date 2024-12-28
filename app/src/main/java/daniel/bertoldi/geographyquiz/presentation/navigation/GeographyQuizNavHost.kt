package daniel.bertoldi.geographyquiz.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import daniel.bertoldi.quiz.presentation.navigateToQuizGame
import daniel.bertoldi.quiz.presentation.quizDestinations

@Composable
fun GeographyQuizNavHost(
    navigationController: NavHostController,
) {
    NavHost(
        navController = navigationController,
        startDestination = ScreenRoutes.Home,
    ) {
        flagGameDestinations(
            onNavigateToQuizGame = {
                navigationController.navigateToQuizGame()
            },
            onNavigateToRegionSelection = {
                navigationController.navigateToRegionSelection()
            },
            onNavigateToAreaSelection = { chosenRegion ->
                navigationController.navigateToAreaSelection(chosenRegion)
            },
            onNavigateToGameModeSelection = { chosenRegion, chosenArea ->
                navigationController.navigateToGameModeSelection(
                    chosenRegion = chosenRegion,
                    chosenArea = chosenArea,
                )
            },
            onNavigateToFlagGame = { chosenRegion, chosenArea, chosenGameMode ->
                navigationController.navigateToFlagGame(
                    chosenRegion = chosenRegion,
                    chosenArea = chosenArea,
                    chosenGameMode = chosenGameMode,
                )
            },
            onPopBackStackToHome = { navigationController.popBackStackToHome() },
            onPopBackStackToRegionSelection = {
                navigationController.popBackStackToRegionSelection()
            }
        )
        quizDestinations()
    }
}