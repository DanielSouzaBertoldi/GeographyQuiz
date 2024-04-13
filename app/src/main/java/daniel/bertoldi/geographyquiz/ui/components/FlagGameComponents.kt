package daniel.bertoldi.geographyquiz.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import daniel.bertoldi.database.CountryEntity
import daniel.bertoldi.geographyquiz.Continent
import daniel.bertoldi.geographyquiz.GameState
import daniel.bertoldi.geographyquiz.GameStep

@Composable
internal fun FlagGameComponent(
    gameState: GameState?,
    optionClick: (CountryEntity) -> Unit,
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
                    model = gameState.availableOptions.find { it.isTheCorrectAnswer }?.countryData?.flagPng,
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
                                } else if (gameState.step == GameStep.OPTION_SELECTED && it.countryData.countryCode == gameState.clickedOption?.countryCode) {
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
                            onClick = {
                                loadingFlag = true
                                drawAgain()
                            }
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
private fun FlagGameComponentPreview() {
    FlagGameComponent(
        gameState = GameState(
            step = GameStep.CHOOSING_OPTION,
            availableOptions = emptyList(),
            // TODO: Create an UI Model. It's pathetic to instantiate CountryEntity here.
            // availableOptions = listOf(
            //    FlagOption(
            //        isTheCorrectAnswer = true,
            //        countryData = CountryEntity()
            //    )
            // ),
            chosenContinent = Continent.SOUTH_AMERICA.simpleName,
            clickedOption = null,
            score = 40,
        ),
        optionClick = {},
        drawAgain = {},
    )
}