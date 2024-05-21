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
import daniel.bertoldi.geographyquiz.presentation.viewmodel.AreaViewModel
import daniel.bertoldi.geographyquiz.presentation.viewmodel.GameModeViewModel
import daniel.bertoldi.geographyquiz.ui.components.ChooseAreaComponent
import daniel.bertoldi.geographyquiz.ui.components.ErrorComponent
import daniel.bertoldi.geographyquiz.ui.components.FlagGameComponent
import daniel.bertoldi.geographyquiz.ui.components.HomeComponent
import daniel.bertoldi.geographyquiz.ui.components.LoadingComponent
import daniel.bertoldi.geographyquiz.ui.components.SelectGameMode
import daniel.bertoldi.geographyquiz.ui.components.SelectRegionComponent
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
                                        navigationController.navigate("regionSelection")
                                    }
                                )

                                is MainScreenState.Failed -> ErrorComponent()
                            }
                        }
                        composable("regionSelection") {
                            SelectRegionComponent {
                                navigationController.navigate("selectArea/${it.simpleName}")
                            }
                        }
                        composable(
                            route = "selectArea/{region}",
                            arguments = listOf(
                                navArgument("region") {
                                    type = NavType.StringType
                                    nullable = false
                                }
                            )
                        ) {
                            val viewModel = hiltViewModel<AreaViewModel>()
                            val region = it.arguments?.getString("region").orEmpty()

                            LaunchedEffect(key1 = region) {
                                viewModel.fetchSubRegions(region)
                            }

                            ChooseAreaComponent(
                                nextStep = { subRegion ->
                                    navigationController.navigate(
                                        route = "gameMode/$region/$subRegion",
                                    )
                                },
                                screenState = viewModel.screenState.collectAsState().value,
                            )
                        }
                        composable(
                            route = "gameMode/{region}/{subRegion}",
                            arguments = listOf(
                                navArgument("region") {
                                    type = NavType.StringType
                                    nullable = false
                                },
                                navArgument("subRegion") {
                                    type = NavType.StringType
                                    nullable = false
                                }
                            ),
                        ) {
                            val viewModel = hiltViewModel<GameModeViewModel>()

                            LaunchedEffect(key1 = Unit) {
                                viewModel.fetchArguments(it.arguments)
                            }

                            SelectGameMode(
                                screenState = viewModel.screenState.collectAsState().value,
                                onGameModeClick = { gameMode, region, subRegion ->
                                    viewModel.confirmSelection(gameMode, region, subRegion)
                                },
                            )
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
