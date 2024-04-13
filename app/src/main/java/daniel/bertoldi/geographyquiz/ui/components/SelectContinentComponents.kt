package daniel.bertoldi.geographyquiz.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import daniel.bertoldi.geographyquiz.Continent
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.ui.theme.BrunswickGreen
import daniel.bertoldi.geographyquiz.ui.theme.OffWhite
import daniel.bertoldi.geographyquiz.ui.theme.Typography

@Composable
internal fun SelectContinentComponent(startGame: (Continent) -> Unit) {
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

@Preview(showBackground = true)
@Composable
private fun SelectContinentComponentPreview() {
    SelectContinentComponent(startGame = {})
}

@Preview(showBackground = true)
@Composable
private fun ContinentCardPreview() {
    ContinentCard(
        continentMap = R.drawable.south_america,
        continentName = Continent.SOUTH_AMERICA.simpleName,
        startGame = {},
    )
}