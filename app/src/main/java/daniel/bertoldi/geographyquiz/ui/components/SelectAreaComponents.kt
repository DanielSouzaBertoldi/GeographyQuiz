package daniel.bertoldi.geographyquiz.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.Region
import daniel.bertoldi.geographyquiz.presentation.viewmodel.AreaScreenState
import daniel.bertoldi.geographyquiz.ui.theme.AliceBlue
import daniel.bertoldi.geographyquiz.ui.theme.BrunswickGreen
import daniel.bertoldi.geographyquiz.ui.theme.CambridgeBlue
import daniel.bertoldi.geographyquiz.ui.theme.Celadon
import daniel.bertoldi.geographyquiz.ui.theme.LightGray
import daniel.bertoldi.geographyquiz.ui.theme.RichBlack
import kotlin.math.roundToInt

@Composable
internal fun ChooseAreaComponent(
    clickableStuff: () -> Unit,
    screenState: AreaScreenState,
) {
    if (screenState is AreaScreenState.Success) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = AliceBlue),
            verticalArrangement = Arrangement.Center,
        ) {
            Step(stringRes = R.string.choose_area)
            GameRulesComponent(
                regionName = screenState.regionData.regionString,
                regionIcon = screenState.regionData.regionIcon,
                shouldAnimateHeader = false,
            )
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp, start = 14.dp, end = 14.dp, bottom = 24.dp),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(6.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items(screenState.regionData.subRegions) {
                    GameOptionCard(
                        icon = it.subRegionIcon,
                        stringRes = it.subRegionName,
                        nextStep = { clickableStuff() },
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "Hold on I'm loading stuff here tho.....")
        }
    }
}

@Composable
private fun GameRulesComponent(
    @StringRes regionName: Int,
    @DrawableRes regionIcon: Int,
    shouldAnimateHeader: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp),
    ) {
        GameRuleHeaderComponent(shouldAnimateHeader)
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = LightGray, shape = RoundedCornerShape(14.dp))
                .border(width = 2.dp, color = BrunswickGreen, shape = RoundedCornerShape(14.dp)),
            columns = GridCells.Fixed(2),
        ) {
            item {
                GameRuleKeyComponent(keyName = "Chosen Continent:")
            }
            item {
                GameRuleValueComponent(valueName = regionName, valueIcon = regionIcon)
            }
        }
    }
}

@Composable
private fun GameRuleHeaderComponent(
    shouldAnimateHeader: Boolean = false,
) {
    var animateHeader by remember { mutableStateOf(false) }
    val finalHeaderYOffset = with(LocalDensity.current) {
        -40.dp.toPx().roundToInt()
    }
    val animatedOffset by animateIntOffsetAsState(
        targetValue = if (animateHeader) IntOffset(0, finalHeaderYOffset) else IntOffset(0, 0),
        animationSpec = tween(
            easing = FastOutSlowInEasing,
            durationMillis = 800,
            delayMillis = 200,
        ),
        label = "header animation"
    )
    val offsetModifier = if (shouldAnimateHeader) {
        Modifier
            .offset(y = 50.dp)
            .offset { animatedOffset }
    } else {
        Modifier.offset(y = 10.dp)
    }
    LaunchedEffect(key1 = Unit) {
        animateHeader = shouldAnimateHeader
    }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .then(offsetModifier)
            .background(
                color = CambridgeBlue,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            )
            .border(
                width = 2.dp,
                color = BrunswickGreen,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            )
            .padding(bottom = 16.dp, top = 10.dp),
        text = stringResource(id = R.string.game_rules),
        fontSize = 24.sp,
        color = AliceBlue,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun GameRuleKeyComponent(
    keyName: String,
) {
    Text(
        modifier = Modifier
            .height(55.dp)
            .background(
                color = RichBlack,
                shape = RoundedCornerShape(bottomStart = 14.dp, topStart = 14.dp),
            )
            .border(
                width = 2.dp,
                color = BrunswickGreen,
                shape = RoundedCornerShape(bottomStart = 14.dp, topStart = 14.dp)
            )
            .padding(horizontal = 30.dp, vertical = 4.dp),
        text = keyName,
        color = Celadon,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun GameRuleValueComponent(
    @StringRes valueName: Int,
    @DrawableRes valueIcon: Int,
) {
    Row(
        modifier = Modifier
            .height(55.dp)
            .background(
                color = Celadon,
                shape = RoundedCornerShape(bottomEnd = 14.dp, topEnd = 14.dp),
            )
            .border(
                width = 2.dp,
                color = BrunswickGreen,
                shape = RoundedCornerShape(bottomEnd = 14.dp, topEnd = 14.dp),
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = valueName),
            color = RichBlack,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
        )
        Image(
            modifier = Modifier
                .size(36.dp),
            painter = painterResource(id = valueIcon),
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChooseAreaComponentPreview() {
    ChooseAreaComponent(clickableStuff = {}, screenState = AreaScreenState.Success(Region.ASIA))
}

@Preview(showBackground = true)
@Composable
private fun GameRulesComponentPreview() {
    GameRulesComponent(regionName = R.string.africa, regionIcon = R.drawable.africa)
}

@Preview(showBackground = true)
@Composable
private fun GameRulesHeaderComponentPreview() {
    GameRuleHeaderComponent(shouldAnimateHeader = false)
}

@Preview(showBackground = true, widthDp = 165)
@Composable
private fun GameRuleKeyComponentPreview() {
    GameRuleKeyComponent("Chosen Continent:")
}

@Preview(showBackground = true, widthDp = 165)
@Composable
private fun GameRuleValueComponentPreview() {
    GameRuleValueComponent(valueName = R.string.africa, valueIcon = R.drawable.africa)
}
