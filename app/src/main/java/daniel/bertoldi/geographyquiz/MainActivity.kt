package daniel.bertoldi.geographyquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import daniel.bertoldi.geographyquiz.ui.components.ChooseRegionComponent
import daniel.bertoldi.geographyquiz.ui.components.ErrorComponent
import daniel.bertoldi.geographyquiz.ui.components.FlagGameComponent
import daniel.bertoldi.geographyquiz.ui.components.HomeComponent
import daniel.bertoldi.geographyquiz.ui.components.LoadingComponent
import daniel.bertoldi.geographyquiz.ui.components.SelectContinentComponent
import daniel.bertoldi.geographyquiz.ui.theme.GeographyQuizTheme

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
                    NavHost(navController = navigationController, startDestination = "home") {
                        composable("home") {
                            val viewModel = hiltViewModel<MainActivityViewModel>()
                            val screenState = viewModel.mainScreenState.collectAsState()

                            when (screenState.value) {
                                is MainScreenState.Loading -> LoadingComponent()
                                is MainScreenState.Success -> HomeComponent(
                                    navigateToGameScreen = {
                                        navigationController.navigate("continentSelection")
                                    }
                                )

                                is MainScreenState.Failed -> ErrorComponent()
                            }
                        }
                        composable("continentSelection") {
                            SelectContinentComponent {
                                navigationController.navigate("selectRegion/${it.simpleName}")
                            }
                        }
                        composable(
                            route = "selectRegion/{continent}",
                            arguments = listOf(
                                navArgument("continent") {
                                    type = NavType.StringType
                                    nullable = false
                                }
                            )
                        ) {
                            ChooseRegionComponent(clickableStuff = {})
                        }
                        composable(
                            route = "flagGame/{continent}",
                            arguments = listOf(
                                navArgument("continent") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                            )
                        ) {
                            val viewModel = hiltViewModel<FlagGameViewModel>()
                            val chosenContinent = it.arguments?.getString("continent").orEmpty()
                            LaunchedEffect(chosenContinent) {
                                viewModel.startFlagGame(chosenContinent)
                            }

                            FlagGameComponent(
                                gameState = viewModel.gameState.collectAsState().value,
                                optionClick = {
                                    clickedOption -> viewModel.optionClick(clickedOption)
                                },
                                drawAgain = { viewModel.drawAgain() }
                            )
                        }
                    }
                }
            }
        }
    }
}
