package daniel.bertoldi.geographyquiz.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import daniel.bertoldi.geographyquiz.Continent
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.ui.theme.AliceBlue
import daniel.bertoldi.geographyquiz.ui.theme.CambridgeBlue
import daniel.bertoldi.geographyquiz.ui.theme.Celadon
import daniel.bertoldi.geographyquiz.ui.theme.Gray
import daniel.bertoldi.geographyquiz.ui.theme.LightGray
import daniel.bertoldi.geographyquiz.ui.theme.RichBlack

@Composable
internal fun SelectContinentComponent(startGame: (Continent) -> Unit) {
    var deviceWidth = LocalConfiguration.current.screenWidthDp.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Step()
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp, start = 14.dp, end = 14.dp)
                .wrapContentSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(6.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                ContinentCard(
                    continent = R.drawable.africa,
                    continentName = stringResource(id = R.string.africa),
                    startGame = { startGame(Continent.AFRICA) },
                )
            }

            item {
                ContinentCard(
                    continent = R.drawable.americas,
                    continentName = stringResource(id = R.string.americas),
                    startGame = { startGame(Continent.EUROPE) },
                )
            }

            item {
                ContinentCard(
                    continent = R.drawable.asia,
                    continentName = stringResource(id = R.string.asia),
                    startGame = { startGame(Continent.ASIA) },
                )
            }

            item {
                ContinentCard(
                    continent = R.drawable.europe,
                    continentName = stringResource(id = R.string.europe),
                    startGame = { startGame(Continent.ASIA) },
                )
            }

            item(
                span = { GridItemSpan(2) }
            ) {
                ContinentCard(
                    modifier = Modifier.padding(horizontal = deviceWidth / 5),
                    continent = R.drawable.australia,
                    continentName = stringResource(id = R.string.oceania),
                    startGame = { startGame(Continent.OCEANIA) },
                )
            }
        }
    }
}

@Composable
private fun Step() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(
                color = CambridgeBlue,
                shape = RoundedCornerShape(24.dp),
            )
            .padding(horizontal = 34.dp, vertical = 16.dp),
        text = "Choose the Continent",
        color = AliceBlue,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun ContinentCard(
    modifier: Modifier = Modifier,
    @DrawableRes continent: Int,
    continentName: String,
    startGame: () -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = { startGame() },
        colors = CardDefaults.cardColors(
            containerColor = LightGray,
        ),
        border = BorderStroke(
            width = 2.dp,
            color = Gray,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(94.dp)
                    .padding(bottom = 8.dp),
                painter = painterResource(id = continent),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(color = Celadon)
                    .border(Dp.Hairline, Gray)
                    .wrapContentSize(),
                text = continentName,
                textAlign = TextAlign.Center,
                color = RichBlack,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StepPreview() {
    Step()
}

@Preview(showBackground = true)
@Composable
private fun SelectContinentComponentPreview() {
    SelectContinentComponent(startGame = {})
}

@Preview(showBackground = true, widthDp = 165)
@Composable
private fun ContinentCardPreview() {
    ContinentCard(
        continent = R.drawable.africa,
        continentName = Continent.AFRICA.simpleName,
        startGame = {},
    )
}