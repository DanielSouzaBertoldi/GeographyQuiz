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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.presentation.ui.theme.AliceBlue
import daniel.bertoldi.geographyquiz.presentation.ui.theme.BrunswickGreen

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
    imageLoader: ImageLoader,
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
            imageLoader = imageLoader,
        )
        Button(
            modifier = Modifier
                .padding(top = 102.dp)
                .height(116.dp)
                .width(296.dp),
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
    HomeComponent(navigateToGameScreen = {}, imageLoader = ImageLoader(LocalContext.current))
}

@Preview(showBackground = true)
@Composable
private fun ErrorComponentPreview() {
    ErrorComponent()
}