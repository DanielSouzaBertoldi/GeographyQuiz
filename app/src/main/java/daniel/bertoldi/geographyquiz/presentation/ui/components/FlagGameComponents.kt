package daniel.bertoldi.geographyquiz.presentation.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.presentation.model.CountryFlagUi
import daniel.bertoldi.geographyquiz.presentation.ui.theme.AliceBlue
import daniel.bertoldi.geographyquiz.presentation.ui.theme.BrunswickGreen
import daniel.bertoldi.geographyquiz.presentation.ui.theme.Celadon
import daniel.bertoldi.geographyquiz.presentation.ui.theme.LightGray
import daniel.bertoldi.geographyquiz.presentation.ui.theme.Poppy
import daniel.bertoldi.geographyquiz.presentation.ui.theme.RichBlack
import daniel.bertoldi.geographyquiz.presentation.ui.theme.Typography
import daniel.bertoldi.geographyquiz.presentation.viewmodel.GameState
import daniel.bertoldi.geographyquiz.presentation.viewmodel.GameStep
import daniel.bertoldi.geographyquiz.presentation.viewmodel.RoundState
import kotlinx.coroutines.delay

@Composable
internal fun FlagGameComponent(
    gameState: GameState,
    optionClick: (String) -> Unit,
    reDrawn: () -> Unit,
    giveUp: () -> Unit,
    onPlayAgain: () -> Unit,
    onRetry: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    BackHandler(enabled = gameState.step != GameStep.END_GAME) { showDialog = true }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = showDialog) {
            GiveUpDialog(
                onConfirm = giveUp,
                onDecline = { showDialog = false },
            )
        }

        AnimatedContent(
            targetState = gameState.step,
            label = "animated content change",
            contentKey = { if (gameState.step != GameStep.END_GAME) "ongoing" else "end" },
        ) { currentGameStep ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AliceBlue)
                    .padding(top = 40.dp)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when (currentGameStep) {
                    GameStep.END_GAME -> {
                        EndGameContent(
                            finalScore = gameState.score,
                            roundState = gameState.roundState,
                            playAgain = onPlayAgain,
                            retry = onRetry,
                        )
                    }

                    else -> {
                        OnGoingGameContent(
                            gameState = gameState,
                            optionClick = optionClick,
                            reDrawn = reDrawn,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OnGoingGameContent(
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

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Score: ${gameState.score}",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        )
        Text(
            text = "Round: ${gameState.roundState.currentRound} / ${gameState.roundState.totalFlags}",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
    }
    AsyncImage(
        modifier = Modifier
            .aspectRatio(2f)
            .padding(top = 30.dp)
            .clip(shape = RoundedCornerShape(15.dp)),
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
            OptionSquareButton(
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

            OptionSquareButton(
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
private fun OptionSquareButton(
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

@Composable
private fun EndGameContent(
    finalScore: Int,
    roundState: RoundState,
    playAgain: () -> Unit,
    retry: () -> Unit,
) {
    val rank = roundState.getGameRank()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .height(170.dp)
                .padding(bottom = 14.dp),
            painter = painterResource(id = rank.image),
            contentDescription = null,
        )

        GeographyQuizTableComponent(
            tableHeaderText = R.string.game_results,
            shouldAnimateHeader = true,
            rules = listOf(
                {
                    GameRuleKeyComponent(
                        keyName = R.string.ranking,
                        cornerShape = RoundedCornerShape(topStart = 14.dp),
                    )
                },
                {
                    GameRuleValueComponent(
                        valueName = stringResource(id = rank.title),
                        cornerShape = RoundedCornerShape(topEnd = 14.dp),
                    )
                },
                {
                    GameRuleKeyComponent(
                        keyName = R.string.final_score,
                        cornerShape = RoundedCornerShape(bottomStart = 0.dp),
                    )
                },
                {
                    GameRuleValueComponent(
                        valueName = finalScore.toString(),
                        cornerShape = RoundedCornerShape(bottomEnd = 0.dp),
                    )
                },
                {
                    GameRuleKeyComponent(
                        keyName = R.string.hits_and_misses,
                        cornerShape = RoundedCornerShape(bottomStart = 0.dp),
                    )
                },
                {
                    GameRuleValueComponent(
                        valueName = "${roundState.hits} / ${roundState.misses}",
                        cornerShape = RoundedCornerShape(bottomEnd = 0.dp),
                    )
                },
                {
                    GameRuleKeyComponent(
                        keyName = R.string.accuracy,
                        cornerShape = RoundedCornerShape(bottomStart = 14.dp),
                    )
                },
                {
                    GameRuleValueComponent(
                        valueName = "${roundState.accuracy}%",
                        cornerShape = RoundedCornerShape(bottomEnd = 14.dp),
                    )
                },
            )
        )

        ActionButton(
            modifier = Modifier.padding(top = 60.dp),
            text = R.string.play_again,
            action = { playAgain() },
            textColor = RichBlack,
            backgroundColor = Celadon,
            fontWeight = FontWeight.Bold,
            textSize = 40.sp,
        )
        ActionButton(
            modifier = Modifier.padding(top = 20.dp),
            text = R.string.retry,
            action = { retry() },
            textColor = AliceBlue,
            backgroundColor = BrunswickGreen,
            fontWeight = FontWeight.Light,
            textSize = 32.sp,
        )
    }
}

@Composable
private fun GiveUpDialog(
    onConfirm: () -> Unit,
    onDecline: () -> Unit,
) {
    Dialog(onDismissRequest = { onDecline() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = RichBlack,
                contentColor = AliceBlue,
            ),
        ) {
            Column(
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(id = R.drawable.broken_earth),
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = "Are you sure you want to quit?",
                    style = Typography.titleLarge,
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    TextButton(
                        modifier = Modifier
                            .background(Celadon)
                            .weight(1f),
                        onClick = { onConfirm() },
                    ) {
                        Text(
                            text = "Hell yeah.",
                            textAlign = TextAlign.Center,
                            color = RichBlack,
                        )
                    }
                    TextButton(
                        modifier = Modifier
                            .background(Poppy)
                            .weight(1f),
                        onClick = { onDecline() },
                    ) {
                        Text(
                            text = "Hell no.",
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlagGameComponentPreview(
    @PreviewParameter(OnGoingGameParameterProvider::class) gameStep: GameStep,
) {
    FlagGameComponent(
        gameState = GameState(
            step = gameStep,
            availableOptions = listOf(
                CountryFlagUi(
                    countryCode = "BR",
                    countryName = "Brazil",
                    flagUrl = "https://flagcdn.com/w320/br.png"
                ),
                CountryFlagUi(
                    countryCode = "AO",
                    countryName = "Angola",
                    flagUrl = "https://flagcdn.com/w320/ao.png"
                ),
                CountryFlagUi(
                    countryCode = "ST",
                    countryName = "São Tomé e Príncipe",
                    flagUrl = "https://flagcdn.com/w320/st.png"
                ),
                CountryFlagUi(
                    countryCode = "PT",
                    countryName = "Portugal",
                    flagUrl = "https://flagcdn.com/w320/pt.png"
                ),
            ),
            score = 40,
            correctCountryCode = "BR",
        ),
        optionClick = {},
        reDrawn = {},
        giveUp = {},
        onPlayAgain = {},
        onRetry = {},
    )
}

@Preview
@Composable
private fun OptionsGridPreview() {
    OptionsGrid(
        gameState = GameState(
            step = GameStep.CHOOSING_OPTION,
             availableOptions = listOf(
                CountryFlagUi(
                    countryCode = "BR",
                    countryName = "Brazil",
                    flagUrl = "https://flagcdn.com/w320/br.png"
                ),
                 CountryFlagUi(
                     countryCode = "AO",
                     countryName = "Angola",
                     flagUrl = "https://flagcdn.com/w320/ao.png"
                 ),
                 CountryFlagUi(
                     countryCode = "ST",
                     countryName = "São Tomé e Príncipe",
                     flagUrl = "https://flagcdn.com/w320/st.png"
                 ),
                 CountryFlagUi(
                     countryCode = "PT",
                     countryName = "Portugal",
                     flagUrl = "https://flagcdn.com/w320/pt.png"
                 ),
             ),
            score = 40,
            correctCountryCode = "BR",
        ),
        optionClick = {},
    )
}

@Preview
@Composable
private fun OptionButtonNextFlagPreview() {
    OptionSquareButton(
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
    OptionSquareButton(
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
    OptionSquareButton(
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
    OptionSquareButton(
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
    OptionSquareButton(
        onClick = {},
        buttonColors = ButtonDefaults.buttonColors(
            disabledContainerColor = Color.Gray,
            disabledContentColor = AliceBlue,
        ),
        isButtonEnabled = false,
        buttonText = "Central African Republic",
    )
}

@Preview(showBackground = true)
@Composable
private fun EndGameContentPreview() {
    EndGameContent(
        finalScore = 200,
        roundState = RoundState(
            currentRound = 10,
            totalFlags = 10,
            hits = 5,
            misses = 5,
        ),
        playAgain = {},
        retry = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun GiveUpComposablePreview() {
    GiveUpDialog(
        onConfirm = {},
        onDecline = {},
    )
}

class OnGoingGameParameterProvider() : PreviewParameterProvider<GameStep> {
    override val values = sequenceOf(
        GameStep.CHOOSING_OPTION,
        GameStep.OPTION_SELECTED,
    )
}
