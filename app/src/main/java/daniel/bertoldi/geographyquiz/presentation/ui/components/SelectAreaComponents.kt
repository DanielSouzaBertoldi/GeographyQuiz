package daniel.bertoldi.geographyquiz.presentation.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.presentation.model.Region
import daniel.bertoldi.geographyquiz.presentation.viewmodel.AreaScreenState
import daniel.bertoldi.geographyquiz.presentation.ui.theme.AliceBlue
import daniel.bertoldi.geographyquiz.presentation.ui.theme.BrunswickGreen
import daniel.bertoldi.geographyquiz.presentation.ui.theme.CambridgeBlue
import kotlin.math.roundToInt

@Composable
internal fun ChooseAreaComponent(
    nextStep: (String) -> Unit,
    screenState: AreaScreenState,
) {
    if (screenState is AreaScreenState.Success) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = AliceBlue)
                .padding(top = 40.dp)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Step(stringRes = R.string.choose_area)
            GameRulesComponent(
                shouldAnimateHeader = true,
                rules = listOf(
                    {
                        GameRuleKeyComponent(
                            keyName = R.string.chosen_region,
                            cornerShape = RoundedCornerShape(bottomStart = 14.dp, topStart = 14.dp),
                        )
                    },
                    {
                        GameRuleValueComponent(
                            valueName = screenState.regionData.regionString,
                            valueIcon = screenState.regionData.regionIcon,
                            cornerShape = RoundedCornerShape(bottomEnd = 14.dp, topEnd = 14.dp),
                        )
                    }
                )
            )
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp, bottom = 24.dp),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(6.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items(screenState.regionData.subRegions) {
                    GameOptionCard(
                        icon = it.subRegionIcon,
                        stringRes = it.subRegionName,
                        nextStep = { nextStep(it.name) },
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
fun GameRuleHeaderComponent(
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

@Preview(showBackground = true)
@Composable
private fun ChooseAreaComponentPreview() {
    ChooseAreaComponent(
        nextStep = {},
        screenState = AreaScreenState.Success(Region.ASIA)
    )
}
