package daniel.bertoldi.geographyquiz.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
internal fun BeginGameComponent(
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
    BeginGameComponent(navigateToGameScreen = {})
}

@Preview(showBackground = true)
@Composable
private fun ErrorComponentPreview() {
    ErrorComponent()
}