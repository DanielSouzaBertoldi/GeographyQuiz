package daniel.bertoldi.geographyquiz.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import daniel.bertoldi.database.CountryEntity
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.presentation.model.Region
import daniel.bertoldi.geographyquiz.presentation.ui.theme.AliceBlue
import daniel.bertoldi.geographyquiz.presentation.ui.theme.Celadon
import daniel.bertoldi.geographyquiz.presentation.ui.theme.LightGray
import daniel.bertoldi.geographyquiz.presentation.ui.theme.Poppy
import daniel.bertoldi.geographyquiz.presentation.ui.theme.RichBlack
import daniel.bertoldi.geographyquiz.presentation.viewmodel.GameState
import daniel.bertoldi.geographyquiz.presentation.viewmodel.GameStep

@Composable
internal fun FlagGameComponent(
    gameState: GameState?,
    optionClick: (CountryEntity) -> Unit,
    drawAgain: () -> Unit,
) {
    var loadingFlag by remember { mutableStateOf(gameState?.step == GameStep.CHOOSING_OPTION) }

    gameState?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = AliceBlue)
                .padding(top = 40.dp)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Score: ${gameState.score}",
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
            )

            AsyncImage(
                modifier = Modifier
                    .width(313.dp)
                    .height(165.dp)
                    .clip(
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(top = 30.dp),
                model = gameState.availableOptions.find { it.isTheCorrectAnswer }?.countryData?.flagPng,
                contentDescription = null,
                onSuccess = { loadingFlag = false },
                contentScale = ContentScale.Fit,
            )
            Text(
                modifier = Modifier.padding(top = 18.dp, bottom = 30.dp),
                text = "What country is this?",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
            if (loadingFlag) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    trackColor = Color.Black,
                )
            } else {
                LazyColumn {
                    items(gameState.availableOptions) {
                        val disabledContainerColor by animateColorAsState(
                            targetValue = if (gameState.step == GameStep.OPTION_SELECTED && it.isTheCorrectAnswer) {
                                Celadon
                            } else if (gameState.step == GameStep.OPTION_SELECTED && it.countryData.countryCode == gameState.clickedOption?.countryCode) {
                                Poppy
                            } else {
                                Color.Gray
                            }
                        )
                        val disabledContentColor by animateColorAsState(
                            targetValue = if (gameState.step == GameStep.OPTION_SELECTED && it.isTheCorrectAnswer) {
                                RichBlack
                            } else {
                                AliceBlue
                            }
                        )

                        OptionButton(
                            onClick = { optionClick(it.countryData) },
                            buttonColors = ButtonDefaults.buttonColors(
                                containerColor = LightGray,
                                contentColor = RichBlack,
                                disabledContainerColor = disabledContainerColor,
                                disabledContentColor = disabledContentColor,
                            ),
                            isButtonEnabled = gameState.step == GameStep.CHOOSING_OPTION,
                            buttonText = it.countryData.name.common,
                        )
                    }
                }
                if (gameState.step == GameStep.OPTION_SELECTED) {
                    OptionButton(
                        onClick = {
                            loadingFlag = true
                            drawAgain()
                        },
                        buttonColors = ButtonDefaults.buttonColors(
                            containerColor = RichBlack,
                            contentColor = AliceBlue,
                        ),
                        isButtonEnabled = true,
                        buttonText = stringResource(id = R.string.next_flag),
                    )
                }
            }
        }
    }
}

@Composable
private fun OptionButton(
    onClick: () -> Unit,
    buttonColors: ButtonColors,
    isButtonEnabled: Boolean,
    buttonText: String,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .border(
                width = Dp.Hairline,
                color = RichBlack,
                shape = RoundedCornerShape(10.dp),
            ),
        onClick = { onClick() },
        colors = buttonColors,
        enabled = isButtonEnabled,
        shape = RoundedCornerShape(10.dp),
    ) {
        Text(
            text = buttonText,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
        )
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
            chosenContinent = Region.AMERICAS.simpleName,
            clickedOption = null,
            score = 40,
        ),
        optionClick = {},
        drawAgain = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun OptionButtonNextFlagPreview() {
    OptionButton(
        onClick = {},
        buttonColors = ButtonDefaults.buttonColors(
            containerColor = RichBlack,
            contentColor = AliceBlue,
        ),
        isButtonEnabled = true,
        buttonText = stringResource(id = R.string.next_flag),
    )
}

@Preview
@Composable
private fun OptionButtonFlagOptionInitialStatePreview() {
    OptionButton(
        onClick = {},
        buttonColors = ButtonDefaults.buttonColors(
            containerColor = LightGray,
            contentColor = RichBlack,
        ),
        isButtonEnabled = true,
        buttonText = "Brazil",
    )
}

@Preview
@Composable
private fun OptionButtonFlagOptionCorrectGuessStatePreview() {
    OptionButton(
        onClick = {},
        buttonColors = ButtonDefaults.buttonColors(
            disabledContainerColor = Celadon,
            disabledContentColor = RichBlack,
        ),
        isButtonEnabled = false,
        buttonText = "Brazil",
    )
}

@Preview
@Composable
private fun OptionButtonFlagOptionWrongGuessStatePreview() {
    OptionButton(
        onClick = {},
        buttonColors = ButtonDefaults.buttonColors(
            disabledContainerColor = Poppy,
            disabledContentColor = AliceBlue,
        ),
        isButtonEnabled = false,
        buttonText = "Brazil",
    )
}

@Preview
@Composable
private fun OptionButtonFlagOptionNotClickedStatePreview() {
    OptionButton(
        onClick = {},
        buttonColors = ButtonDefaults.buttonColors(
            disabledContainerColor = Color.Gray,
            disabledContentColor = AliceBlue,
        ),
        isButtonEnabled = false,
        buttonText = "Brazil",
    )
}