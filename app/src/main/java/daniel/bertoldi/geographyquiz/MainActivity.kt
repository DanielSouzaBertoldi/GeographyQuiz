package daniel.bertoldi.geographyquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import daniel.bertoldi.geographyquiz.presentation.navigation.ScreenRoutes
import daniel.bertoldi.geographyquiz.presentation.navigation.flagGameDestinations
import daniel.bertoldi.geographyquiz.presentation.navigation.navigateToAreaSelection
import daniel.bertoldi.geographyquiz.presentation.navigation.navigateToFlagGame
import daniel.bertoldi.geographyquiz.presentation.navigation.navigateToGameModeSelection
import daniel.bertoldi.geographyquiz.presentation.navigation.navigateToRegionSelection
import daniel.bertoldi.geographyquiz.presentation.navigation.popBackStackToHome
import daniel.bertoldi.geographyquiz.presentation.navigation.popBackStackToRegionSelection
import daniel.bertoldi.geographyquiz.presentation.ui.theme.GeographyQuizTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GeographyQuizTheme {
                val navigationController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navigationController,
                        startDestination = ScreenRoutes.Home,
                    ) {
                        flagGameDestinations(
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
                    }
                }
            }
        }
    }
}
