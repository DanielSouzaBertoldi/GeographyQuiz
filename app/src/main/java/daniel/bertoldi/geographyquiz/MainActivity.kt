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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import daniel.bertoldi.database.DatabaseStuff
import daniel.bertoldi.geographyquiz.ui.theme.BrunswickGreen
import daniel.bertoldi.geographyquiz.ui.theme.GeographyQuizTheme
import daniel.bertoldi.geographyquiz.ui.theme.OffWhite
import daniel.bertoldi.geographyquiz.ui.theme.Typography
import javax.inject.Inject

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
                                        navigationController.navigate("continent")
                                    }
                                )
                                is MainScreenState.Failed -> ErrorComponent()
                                is MainScreenState.SelectContinent -> SelectContinentComponent({})
                                is MainScreenState.StartGame -> WeDoingThisComponent(
                                    gameState = viewModel.gameState.collectAsState().value,
                                    optionClick = viewModel::optionClick,
                                    drawAgain = viewModel::drawAgain,
                                )
                            }
                        }
                        composable("continent") {
                            val viewModel = hiltViewModel<ContinentScreenViewModel>()
//                            SelectContinentComponent(viewModel::startFlagGame)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingComponent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = MaterialTheme.colorScheme.onSurface,
            trackColor = MaterialTheme.colorScheme.background,
        )
    }
}

@Composable
private fun BeginGameComponent(
    navigateToGameScreen: () -> Unit,
) {
    Column {
        Text(text = "Welcome to Geography Quiz!")
        Button(
            onClick = { navigateToGameScreen() }
        ) {
            Text(text = "Let's begin!")
        }
    }
}

@Composable
private fun ErrorComponent() {
    Text(
        modifier = Modifier.fillMaxSize(),
        text = "Yo something went really bad. Check the logs.",
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun SelectContinentComponent(startGame: (Continent) -> Unit) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(BrunswickGreen),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Select a Continent",
                style = Typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = OffWhite,
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                ContinentCard(
                    continentMap = R.drawable.south_america,
                    continentName = "South America",
                    startGame = { startGame(Continent.SOUTH_AMERICA) },
                )
            }

            item {
                ContinentCard(
                    continentMap = R.drawable.africa,
                    continentName = "Africa",
                    startGame = { startGame(Continent.AFRICA) },
                )
            }

            item {
                ContinentCard(
                    continentMap = R.drawable.europe,
                    continentName = "Europe",
                    startGame = { startGame(Continent.EUROPE) },
                )
            }

            item {
                ContinentCard(
                    continentMap = R.drawable.north_america,
                    continentName = "North & Central America",
                    startGame = { startGame(Continent.NORTH_AMERICA) },
                )
            }

            item {
                ContinentCard(
                    continentMap = R.drawable.asia,
                    continentName = "Asia",
                    startGame = { startGame(Continent.ASIA) },
                )
            }

            item {
                ContinentCard(
                    continentMap = R.drawable.oceania,
                    continentName = "Oceania",
                    startGame = { startGame(Continent.OCEANIA) },
                )
            }
        }
    }
}

@Composable
private fun ContinentCard(
    @DrawableRes continentMap: Int,
    continentName: String,
    startGame: () -> Unit,
) {
    Card(
        onClick = { startGame() },
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            painter = painterResource(id = continentMap),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = continentName,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun WeDoingThisComponent(
    gameState: GameState?,
    optionClick: (BaseCountryDataResponse) -> Unit,
    drawAgain: () -> Unit,
) {
    var loadingFlag by remember { mutableStateOf(gameState?.step == GameStep.CHOOSING_OPTION) }

    gameState?.let {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(text = "Score: ${gameState.score}")

                AsyncImage(
                    model = gameState.availableOptions.find { it.isTheCorrectAnswer }?.countryData?.flags?.png,
                    contentDescription = null,
                    onSuccess = { loadingFlag = false }
                )
                if (loadingFlag) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        trackColor = Color.Black,
                    )
                } else {
                    Text(text = "What's this flag?")
                    LazyColumn {
                        items(gameState.availableOptions) {
                            val backgroundColor by animateColorAsState(
                                targetValue = if (gameState.step == GameStep.OPTION_SELECTED && it.isTheCorrectAnswer) {
                                    Color.Green
                                } else if (gameState.step == GameStep.OPTION_SELECTED && it.countryData.countryCode == gameState.clickedOption?.countryCode){
                                    Color.Red
                                } else {
                                    Color.Gray
                                }
                            )

                            Button(
                                onClick = { optionClick(it.countryData) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black,
                                    disabledContainerColor = backgroundColor,
                                ),
                                enabled = gameState.step == GameStep.CHOOSING_OPTION,
                            ) {
                                Text(text = it.countryData.name.common)
                            }
                        }
                    }
                    if (gameState.step == GameStep.OPTION_SELECTED) {
                        Button(
                            onClick = { drawAgain() }
                        ) {
                            Text(text = "Sortear novamente")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingComponentPreview() {
    LoadingComponent()
}

@Preview(showBackground = true)
@Composable
private fun BeginGameComponentPreview() {
    BeginGameComponent(navigateToGameScreen = {})
}

@Preview(showBackground = true)
@Composable
private fun ErrorComponentPreview() {
    ErrorComponent()
}
