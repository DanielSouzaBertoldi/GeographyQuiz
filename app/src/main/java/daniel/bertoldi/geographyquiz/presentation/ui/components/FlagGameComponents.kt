package daniel.bertoldi.geographyquiz.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.presentation.ui.theme.AliceBlue
import daniel.bertoldi.geographyquiz.presentation.ui.theme.Celadon
import daniel.bertoldi.geographyquiz.presentation.ui.theme.LightGray
import daniel.bertoldi.geographyquiz.presentation.ui.theme.Poppy
import daniel.bertoldi.geographyquiz.presentation.ui.theme.RichBlack
import daniel.bertoldi.geographyquiz.presentation.ui.theme.Typography
import daniel.bertoldi.geographyquiz.presentation.viewmodel.GameState
import daniel.bertoldi.geographyquiz.presentation.viewmodel.GameStep
import kotlinx.coroutines.delay

@Composable
internal fun FlagGameComponent(
    gameState: GameState,
    optionClick: (String) -> Unit,
    reDrawn: () -> Unit,
) {
    var loadingFlag by remember { mutableStateOf(gameState.step == GameStep.CHOOSING_OPTION) }
    var dots by remember { mutableIntStateOf(0) }
    val loadingText = "Loading${".".repeat(dots)}"
    LaunchedEffect(key1 = loadingFlag) {
        while (loadingFlag) {
            dots = ++dots % 4
            delay(400)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AliceBlue)
            .padding(top = 40.dp)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Score: ${gameState.score}",
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
            )
            Text(
                text = "Round: ${gameState.round} / ${gameState.limit}",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
        }

        when (gameState.step) {
            GameStep.END_GAME -> {
                Text(text = "Parabéns vc acabar de ganhar \uD83D\uDC4D")
            }
            else -> {
                AsyncImage(
                    modifier = Modifier
                        .aspectRatio(2f)
                        .padding(top = 30.dp)
                        .clip(
                            shape = RoundedCornerShape(15.dp)
                        ),
                    model = gameState.availableOptions.find { it.countryCode == gameState.correctCountryCode }?.flagUrl,
                    contentDescription = null,
                    onSuccess = { loadingFlag = false },
                    contentScale = if (loadingFlag) ContentScale.Crop else ContentScale.FillBounds,
                    placeholder = painterResource(id = R.drawable.flags_placeholder),
                )
                if (loadingFlag) {
                    Text(
                        modifier = Modifier.padding(top = 18.dp, bottom = 30.dp),
                        text = loadingText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                    )
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(top = 60.dp)
                            .size(64.dp),
                        color = AliceBlue,
                        trackColor = RichBlack,
                    )
                } else {
                    Text(
                        modifier = Modifier.padding(top = 18.dp, bottom = 30.dp),
                        text = stringResource(id = R.string.which_country),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                    )
                    OptionsGrid(gameState, optionClick)
                    if (gameState.step == GameStep.OPTION_SELECTED) {
                        OptionButton(
                            modifier = Modifier.padding(top = 16.dp),
                            onClick = {
                                loadingFlag = true
                                reDrawn()
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
}

@Composable
private fun OptionsGrid(gameState: GameState, optionClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        userScrollEnabled = false,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        items(gameState.availableOptions) {
            val disabledContainerColor by animateColorAsState(
                targetValue = if (gameState.userHasChosen() && gameState.isCorrectAnswer(it.countryCode)) {
                    Celadon
                } else if (gameState.userHasChosen() && gameState.wrongUserOption(it.countryCode)) {
                    Poppy
                } else {
                    Color.Gray
                },
                label = "disabledContainerAnimation",
            )
            val disabledContentColor by animateColorAsState(
                targetValue = if (gameState.userHasChosen() && gameState.isCorrectAnswer(it.countryCode)) {
                    RichBlack
                } else {
                    AliceBlue
                },
                label = "disabledContentAnimation",
            )

            OptionButton(
                modifier = Modifier.aspectRatio(1f),
                onClick = { optionClick(it.countryCode) },
                buttonColors = ButtonDefaults.buttonColors(
                    containerColor = LightGray,
                    contentColor = RichBlack,
                    disabledContainerColor = disabledContainerColor,
                    disabledContentColor = disabledContentColor,
                ),
                isButtonEnabled = gameState.step == GameStep.CHOOSING_OPTION,
                buttonText = it.countryName,
            )
        }
    }
}

@Composable
private fun OptionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    buttonColors: ButtonColors,
    isButtonEnabled: Boolean,
    buttonText: String,
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
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
            modifier = Modifier,
            text = buttonText,
            fontWeight = FontWeight.Medium,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            style = Typography.titleLarge.copy(
                lineBreak = LineBreak.Heading,
            ),
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
            score = 40,
            correctCountryCode = "BR",
        ),
        optionClick = {},
        reDrawn = {},
    )
}

@Preview
@Composable
private fun OptionsGridPreview() {
    OptionsGrid(
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
            score = 40,
            correctCountryCode = "BR",
        ),
        optionClick = {},
    )
}

@Preview
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
        buttonText = "Central African Republic",
    )
}