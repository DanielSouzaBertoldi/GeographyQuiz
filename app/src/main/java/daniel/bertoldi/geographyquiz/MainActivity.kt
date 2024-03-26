package daniel.bertoldi.geographyquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import daniel.bertoldi.geographyquiz.ui.theme.BrunswickGreen
import daniel.bertoldi.geographyquiz.ui.theme.GeographyQuizTheme
import daniel.bertoldi.geographyquiz.ui.theme.OffWhite
import daniel.bertoldi.geographyquiz.ui.theme.Typography

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init()

        setContent {
            GeographyQuizTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val screenState = viewModel.state.collectAsState()
                    when (screenState.value) {
                        is ScreenState.Loading -> LoadingComponent()
                        is ScreenState.Success -> BeginGameComponent(viewModel::startGame)
                        is ScreenState.Failed -> ErrorComponent()
                        is ScreenState.SelectContinent -> SelectContinentComponent(viewModel::startActualGame)
                        is ScreenState.StartGame -> WeDoingThisComponent(
                            countries = viewModel.countries.collectAsState().value
                        )
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
    startGame: () -> Unit,
) {
    Column {
        Text(text = "Welcome to Geography Quiz!")
        Button(
            onClick = { startGame() }
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
private fun WeDoingThisComponent(countries: List<BaseCountryDataResponse>) {
    val selectedFlags = countries.shuffled().take(5)
    val currentFlag by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        AsyncImage(
            model = selectedFlags[currentFlag].flags.png,
            contentDescription = null,
        )
        Text(text = "What's this flag?")
        Button(onClick = { /*TODO*/ }) {
            Text(text = countries.random().name.common)
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = countries.random().name.common)
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = countries.random().name.common)
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = selectedFlags[currentFlag].name.common)
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
    BeginGameComponent(startGame = {})
}

@Preview(showBackground = true)
@Composable
private fun ErrorComponentPreview() {
    ErrorComponent()
}
