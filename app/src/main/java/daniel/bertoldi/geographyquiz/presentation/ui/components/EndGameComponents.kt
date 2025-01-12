package daniel.bertoldi.geographyquiz.presentation.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.presentation.model.GameMode
import daniel.bertoldi.geographyquiz.presentation.model.HighScoresUIModel
import daniel.bertoldi.utilities.design.tokens.AliceBlue
import daniel.bertoldi.utilities.design.tokens.BrunswickGreen
import daniel.bertoldi.utilities.design.tokens.Celadon
import daniel.bertoldi.utilities.design.tokens.RichBlack
import daniel.bertoldi.geographyquiz.presentation.viewmodel.GameRank
import daniel.bertoldi.geographyquiz.presentation.viewmodel.RoundState
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun EndGameContent(
    finalScore: Int,
    roundState: RoundState,
    gameMode: GameMode,
    duration: Duration,
    highScores: List<HighScoresUIModel>,
    playAgain: () -> Unit,
    retry: () -> Unit,
) {
    val rank = roundState.getGameRank()
    Column(
        modifier = Modifier
            .fillMaxSize(),
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

        GameStatsAndHighScoresComponent(
            gameMode = gameMode,
            gameRank = rank,
            roundState = roundState,
            finalScore = finalScore,
            duration = duration,
            highScores = highScores,
        )

        ActionButton(
            modifier = Modifier
                .padding(top = 60.dp)
                .padding(horizontal = 24.dp),
            text = R.string.play_again,
            action = { playAgain() },
            textColor = RichBlack,
            backgroundColor = Celadon,
            fontWeight = FontWeight.Bold,
            textSize = 40.sp,
        )
        ActionButton(
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(horizontal = 24.dp),
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
private fun GameStatsAndHighScoresComponent(
    gameMode: GameMode,
    gameRank: GameRank,
    roundState: RoundState,
    finalScore: Int,
    duration: Duration,
    highScores: List<HighScoresUIModel>,
) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    LaunchedEffect(key1 = Unit) {
        delay(600)
        pagerState.animateScrollBy(130f)
        delay(400)
        pagerState.animateScrollBy(-130f)
    }

    HorizontalPager(
        state = pagerState,
        pageSpacing = 10.dp,
        contentPadding = PaddingValues(horizontal = 10.dp),
    ) { page ->
        when (page) {
            0 -> GameStatsComponent(gameMode, gameRank, roundState, finalScore, duration)
            1 -> HighScoresComponent(highScores = highScores)
        }
    }
    Row(
        Modifier
            .padding(top = 8.dp)
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Had to avoid using pagerState.pageCount since it breaks instrumented tests :think:
        repeat(2) { iteration ->
            AnimatedContent(
                targetState = pagerState.currentPage == iteration,
                label = "Page Indicator Animation",
            ) { content ->
                when (content) {
                    true -> {
                        ActivePageIndicator()
                    }

                    false -> {
                        InactivePageIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun GameStatsComponent(
    gameMode: GameMode,
    gameRank: GameRank,
    roundState: RoundState,
    finalScore: Int,
    duration: Duration,
) {
    TableComponent(
        tableHeaderLeadingIcon = gameMode.icon,
        tableHeaderText = R.string.game_results,
        shouldAnimateHeader = true,
        tableContent = buildList {
            add(
                TableData(
                    TableKey(name = stringResource(id = R.string.ranking)),
                    TableValue(
                        name = stringResource(id = gameRank.title)
                    )
                )
            )
            add(
                TableData(
                    TableKey(name = stringResource(R.string.final_score)),
                    TableValue(name = finalScore.toString())
                )
            )
            add(
                TableData(
                    TableKey(name = stringResource(R.string.hits_and_misses)),
                    TableValue(name = "${roundState.hits} / ${roundState.misses}")
                )
            )
            if (gameMode is GameMode.TimeAttack) {
                add(
                    TableData(
                        TableKey(name = stringResource(id = R.string.time_elapsed)),
                        TableValue(name = duration.inWholeSeconds.seconds.toString()),
                    )
                )
            }
            add(
                TableData(
                    TableKey(name = stringResource(R.string.accuracy)),
                    TableValue(name = "${"%.2f".format(Locale.ROOT, roundState.accuracy)}%"),
                )
            )
        },
    )
}

@Composable
private fun HighScoresComponent(
    highScores: List<HighScoresUIModel>,
) {
    TableComponent(
        tableHeaderText = R.string.high_scores,
        shouldAnimateHeader = false,
        tableContent = buildList {
            highScores.forEach { highScore ->
                add(
                    TableData(
                        key = TableKey(name = highScore.datePlayed),
                        value = TableValue(name = highScore.score.toString()),
                    )
                )
            }
        },
    )
}

@Composable
private fun ActivePageIndicator() {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .clip(CircleShape)
            .background(RichBlack)
            .size(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .padding(2.dp)
                .clip(CircleShape)
                .background(Celadon)
                .size(10.dp)
        )
    }
}

@Composable
private fun InactivePageIndicator() {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .clip(CircleShape)
            .background(RichBlack)
            .size(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun GameStatsAndHighScoresComponentPreview() {
    Column(
        modifier = Modifier.padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GameStatsAndHighScoresComponent(
            gameMode = GameMode.Casual(),
            gameRank = GameRank.GOOD,
            roundState = RoundState(
                currentRound = 10,
                totalFlags = 10,
                hits = 5,
                misses = 5,
            ),
            finalScore = 200,
            duration = 0.seconds,
            highScores = listOf(
                HighScoresUIModel(
                    score = 100,
                    accuracy = 100f,
                    hits = 10,
                    misses = 0,
                    timeElapsed = ZERO,
                    datePlayed = "25/09/2024",
                )
            ),
        )
    }
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
        gameMode = GameMode.Casual(),
        duration = ZERO,
        highScores = listOf(
            HighScoresUIModel(
                score = 100,
                accuracy = 100f,
                hits = 10,
                misses = 0,
                timeElapsed = ZERO,
                datePlayed = "25/09/2024",
            )
        ),
        playAgain = {},
        retry = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun GameStatsComponentPreview() {
    GameStatsComponent(
        gameMode = GameMode.Casual(),
        gameRank = GameRank.GOOD,
        roundState = RoundState(
            currentRound = 10,
            totalFlags = 10,
            hits = 5,
            misses = 5,
        ),
        finalScore = 200,
        duration = 0.seconds,
    )
}

@Preview(showBackground = true)
@Composable
private fun HighScoresComponentPreview() {
    HighScoresComponent(
        highScores = listOf(
            HighScoresUIModel(
                score = 100,
                accuracy = 100f,
                hits = 10,
                misses = 0,
                timeElapsed = ZERO,
                datePlayed = "25/09/2024",
            )
        ),
    )
}


@Preview(showBackground = true)
@Composable
private fun PageIndicatorPreview() {
    Row {
        ActivePageIndicator()
        InactivePageIndicator()
    }
}