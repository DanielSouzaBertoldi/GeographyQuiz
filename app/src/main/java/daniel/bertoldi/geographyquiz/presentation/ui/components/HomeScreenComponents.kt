package daniel.bertoldi.geographyquiz.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.quiz.R as QuizR
import daniel.bertoldi.utilities.design.tokens.AliceBlue
import daniel.bertoldi.utilities.design.tokens.BrunswickGreen

const val HOME_SCREEN_TEST_TAG = "HomeScreen"
const val PLAY_BUTTON_TEST_TAG = "PlayButton"

@Composable
internal fun LoadingComponent() {
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
internal fun HomeComponent(
    navigateToGameScreen: () -> Unit,
    navigateToQuiz: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AliceBlue),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AsyncImage(
            modifier = Modifier.size(300.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.raw.globe3)
                .decoderFactory(ImageDecoderDecoder.Factory())
                .build(),
            contentDescription = null,
        )
        Button(
            modifier = Modifier
                .padding(top = 102.dp)
                .height(116.dp)
                .width(296.dp)
                .testTag(PLAY_BUTTON_TEST_TAG),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrunswickGreen,
                contentColor = AliceBlue,
            ),
            onClick = { navigateToGameScreen() },
            shape = RoundedCornerShape(24.dp),
        ) {
            Text(
                text = stringResource(id = R.string.start_game),
                fontSize = 64.sp,
            )
        }
        Button(
            modifier = Modifier
                .padding(top = 10.dp)
                .height(116.dp)
                .width(296.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrunswickGreen,
                contentColor = AliceBlue,
            ),
            onClick = { navigateToQuiz() },
            shape = RoundedCornerShape(24.dp),
        ) {
            Text(
                text = stringResource(id = QuizR.string.go_to_quiz),
                fontSize = 32.sp,
            )
        }
    }
}

@Composable
internal fun ErrorComponent() {
    Text(
        modifier = Modifier.fillMaxSize(),
        text = "Yo something went really bad. Check the logs.",
        textAlign = TextAlign.Center,
    )
}

@Preview(showBackground = true)
@Composable
private fun LoadingComponentPreview() {
    LoadingComponent()
}

@Preview(showBackground = true)
@Composable
private fun BeginGameComponentPreview() {
    HomeComponent(navigateToGameScreen = {}, navigateToQuiz = {})
}

@Preview(showBackground = true)
@Composable
private fun ErrorComponentPreview() {
    ErrorComponent()
}