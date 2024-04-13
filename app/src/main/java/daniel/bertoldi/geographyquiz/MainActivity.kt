package daniel.bertoldi.geographyquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import daniel.bertoldi.database.CountryEntity
import daniel.bertoldi.geographyquiz.ui.components.BeginGameComponent
import daniel.bertoldi.geographyquiz.ui.components.ErrorComponent
import daniel.bertoldi.geographyquiz.ui.components.FlagGameComponent
import daniel.bertoldi.geographyquiz.ui.components.LoadingComponent
import daniel.bertoldi.geographyquiz.ui.components.SelectContinentComponent
import daniel.bertoldi.geographyquiz.ui.theme.BrunswickGreen
import daniel.bertoldi.geographyquiz.ui.theme.GeographyQuizTheme
import daniel.bertoldi.geographyquiz.ui.theme.OffWhite
import daniel.bertoldi.geographyquiz.ui.theme.Typography

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
                                is MainScreenState.Success -> BeginGameComponent(
                                    navigateToGameScreen = {
                                        navigationController.navigate("continentSelection")
                                    }
                                )

                                is MainScreenState.Failed -> ErrorComponent()
                            }
                        }
                        composable("continentSelection") {
                            SelectContinentComponent {
                                navigationController.navigate("flagGame/${it.simpleName}")
                            }
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
                            LaunchedEffect(it.arguments?.getString("continent")) {
                                viewModel.startFlagGame(it.arguments?.getString("continent").orEmpty())
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
