package daniel.bertoldi.geographyquiz.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import daniel.bertoldi.geographyquiz.Region
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.ui.theme.AliceBlue
import daniel.bertoldi.geographyquiz.ui.theme.BrunswickGreen
import daniel.bertoldi.geographyquiz.ui.theme.CambridgeBlue
import daniel.bertoldi.geographyquiz.ui.theme.Celadon
import daniel.bertoldi.geographyquiz.ui.theme.Gray
import daniel.bertoldi.geographyquiz.ui.theme.LightGray
import daniel.bertoldi.geographyquiz.ui.theme.RichBlack

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
        modifier = modifier,
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
                    modifier = Modifier.padding(start = 10.dp).weight(weight = 1f, fill = false),
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
            .padding(horizontal = 24.dp)
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
fun GameRulesComponent(
    shouldAnimateHeader: Boolean = false,
    rules: List<@Composable ColumnScope.() -> Unit>,
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
            items(rules) {
                it()
            }
        }
    }
}

@Composable
fun GameRuleKeyComponent(
    @StringRes keyName: Int,
    cornerShape: RoundedCornerShape,
) {
    Text(
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
            )
            .padding(horizontal = 30.dp, vertical = 4.dp),
        text = stringResource(id = keyName),
        color = Celadon,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun GameRuleValueComponent(
    @StringRes valueName: Int,
    @DrawableRes valueIcon: Int,
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
    GameRulesComponent(
        shouldAnimateHeader = false,
        rules = listOf(
            {
                GameRuleKeyComponent(
                    keyName = R.string.chosen_region,
                    cornerShape = RoundedCornerShape(bottomStart = 14.dp, topStart = 14.dp),
                )
            },
            {
                GameRuleValueComponent(
                    valueName = R.string.americas,
                    valueIcon = R.drawable.all_americas,
                    cornerShape = RoundedCornerShape(bottomEnd = 14.dp, topEnd = 14.dp),
                )
            }
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun GameRulesHeaderComponentPreview() {
    GameRuleHeaderComponent(shouldAnimateHeader = false)
}

@Preview(showBackground = true, widthDp = 165)
@Composable
private fun GameRuleKeyComponentPreview() {
    GameRuleKeyComponent(
        keyName = R.string.chosen_region,
        cornerShape = RoundedCornerShape(bottomStart = 14.dp, topStart = 14.dp),
    )
}

@Preview(showBackground = true, widthDp = 165)
@Composable
private fun GameRuleValueComponentPreview() {
    GameRuleValueComponent(
        valueName = R.string.africa,
        valueIcon = R.drawable.africa,
        cornerShape = RoundedCornerShape(bottomEnd = 14.dp, topEnd = 14.dp),
    )
}