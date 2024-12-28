package daniel.bertoldi.geographyquiz.presentation.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import daniel.bertoldi.geographyquiz.presentation.model.Region
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.utilities.design.tokens.AliceBlue
import daniel.bertoldi.utilities.design.tokens.BrunswickGreen
import daniel.bertoldi.utilities.design.tokens.CambridgeBlue
import daniel.bertoldi.utilities.design.tokens.Celadon
import daniel.bertoldi.utilities.design.tokens.Gray
import daniel.bertoldi.utilities.design.tokens.LightGray
import daniel.bertoldi.utilities.design.tokens.RichBlack
import kotlin.math.roundToInt

const val GAME_OPTION_TEST_TAG = "GameOption"

@Composable
fun GameOptionCard(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    @StringRes stringRes: Int,
    nextStep: () -> Unit,
    showHelpIcon: Boolean = false,
    helpIconAction: () -> Unit = {},
) {
    Card(
        modifier = modifier.testTag(GAME_OPTION_TEST_TAG),
        onClick = { nextStep() },
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
                painter = painterResource(id = icon),
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(color = Celadon)
                    .border(Dp.Hairline, Gray)
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(weight = 1f, fill = false),
                    text = stringResource(id = stringRes),
                    textAlign = TextAlign.Center,
                    color = RichBlack,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                if (showHelpIcon) {
                    IconButton(onClick = { helpIconAction() }) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null, // TODO: add content description.
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Step(
    @StringRes stringRes: Int,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = CambridgeBlue,
                shape = RoundedCornerShape(24.dp),
            )
            .padding(horizontal = 34.dp, vertical = 16.dp),
        text = stringResource(id = stringRes),
        color = AliceBlue,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun TableHeaderComponent(
    @DrawableRes tableHeaderLeadingIcon: Int?,
    @StringRes tableHeaderText: Int,
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

    Row(
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
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (tableHeaderLeadingIcon != null) {
            Image(
                modifier = Modifier
                    .padding(end = 10.dp, bottom = 5.dp)
                    .size(30.dp)
                    .rotate(-30f),
                painter = painterResource(id = tableHeaderLeadingIcon),
                contentDescription = null,
            )
        }

        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            text = stringResource(id = tableHeaderText),
            fontSize = 24.sp,
            color = AliceBlue,
            textAlign = TextAlign.Center,
        )
    }
}

// TODO: let time do its thing, maybe it's not worth it to have these models separate just
//  for context sake
data class TableData(val key: TableKey, val value: TableValue)
data class TableKey(val name: String, @DrawableRes val icon: Int? = null)
data class TableValue(val name: String, @DrawableRes val icon: Int? = null)

@Composable
fun TableComponent(
    modifier: Modifier = Modifier,
    @DrawableRes tableHeaderLeadingIcon: Int? = null,
    @StringRes tableHeaderText: Int,
    shouldAnimateHeader: Boolean = false,
    tableContent: List<TableData>,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
    ) {
        TableHeaderComponent(tableHeaderLeadingIcon, tableHeaderText, shouldAnimateHeader)
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = LightGray, shape = RoundedCornerShape(14.dp))
                .border(width = 2.dp, color = BrunswickGreen, shape = RoundedCornerShape(14.dp)),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.Center,
        ) {
            for ((idx, tableData) in tableContent.withIndex()) {
                item(
                    key = "${tableData.key.name}$idx",
                ) {
                    GameRuleKeyComponent(
                        keyName = tableData.key.name,
                        cornerShape = RoundedCornerShape(
                            topStart = if (idx == 0) 14.dp else 0.dp,
                            bottomStart = if (idx == tableContent.size - 1) 14.dp else 0.dp,
                        )
                    )
                }

                item(
                    key = "${tableData.value.name}$idx",
                ) {
                    GameRuleValueComponent(
                        valueName = tableData.value.name,
                        valueIcon = tableData.value.icon,
                        cornerShape = RoundedCornerShape(
                            topEnd = if (idx == 0) 14.dp else 0.dp,
                            bottomEnd = if (idx == tableContent.size - 1) 14.dp else 0.dp,
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun GameRuleKeyComponent(
    keyName: String,
    cornerShape: RoundedCornerShape,
) {
    Box(
        modifier = Modifier
            .height(55.dp)
            .background(
                color = RichBlack,
                shape = cornerShape,
            )
            .border(
                width = 2.dp,
                color = BrunswickGreen,
                shape = cornerShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 4.dp),
            text = keyName,
            color = Celadon,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun GameRuleValueComponent(
    valueName: String,
    @DrawableRes valueIcon: Int? = null,
    cornerShape: RoundedCornerShape,
) {
    Row(
        modifier = Modifier
            .height(55.dp)
            .background(
                color = Celadon,
                shape = cornerShape,
            )
            .border(
                width = 2.dp,
                color = BrunswickGreen,
                shape = cornerShape,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = valueName,
            color = RichBlack,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
        )
        if (valueIcon != null) {
            Image(
                modifier = Modifier
                    .size(36.dp),
                painter = painterResource(id = valueIcon),
                contentDescription = null,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 165)
@Composable
private fun ContinentCardPreview() {
    GameOptionCard(
        icon = Region.AFRICA.regionIcon,
        stringRes = Region.AFRICA.regionString,
        nextStep = {},
        showHelpIcon = true,
    )
}

@Preview(showBackground = true)
@Composable
private fun StepPreview() {
    Step(R.string.choose_region)
}

@Preview(showBackground = true)
@Composable
private fun GameRulesComponentPreview() {
    TableComponent(
        tableHeaderText = R.string.game_rules,
        shouldAnimateHeader = false,
        tableContent = listOf(
            TableData(
                TableKey(name = stringResource(id = R.string.chosen_region)),
                TableValue(
                    name = stringResource(id = R.string.americas),
                    icon = R.drawable.americas,
                )
            ),
            TableData(
                TableKey(name = stringResource(id = R.string.chosen_area)),
                TableValue(
                    name = stringResource(id = R.string.all),
                    icon = R.drawable.all_americas,
                )
            )
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun GameRulesHeaderComponentPreview() {
    TableHeaderComponent(
        tableHeaderLeadingIcon = R.drawable.casual,
        tableHeaderText = R.string.game_rules,
        shouldAnimateHeader = false,
    )
}

@Preview(showBackground = true, widthDp = 165)
@Composable
private fun GameRuleKeyComponentPreview() {
    GameRuleKeyComponent(
        keyName = stringResource(id = R.string.chosen_region),
        cornerShape = RoundedCornerShape(bottomStart = 14.dp, topStart = 14.dp),
    )
}

@Preview(showBackground = true, widthDp = 165)
@Composable
private fun GameRuleValueComponentPreview() {
    GameRuleValueComponent(
        valueName = stringResource(R.string.africa),
        valueIcon = R.drawable.africa,
        cornerShape = RoundedCornerShape(bottomEnd = 14.dp, topEnd = 14.dp),
    )
}