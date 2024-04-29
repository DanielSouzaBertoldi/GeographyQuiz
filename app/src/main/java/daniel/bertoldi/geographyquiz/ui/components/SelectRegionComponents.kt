package daniel.bertoldi.geographyquiz.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.ui.theme.AliceBlue
import daniel.bertoldi.geographyquiz.ui.theme.BrunswickGreen
import daniel.bertoldi.geographyquiz.ui.theme.CambridgeBlue
import daniel.bertoldi.geographyquiz.ui.theme.Celadon
import daniel.bertoldi.geographyquiz.ui.theme.LightGray
import daniel.bertoldi.geographyquiz.ui.theme.RichBlack

@Composable
internal fun ChooseRegionComponent(clickableStuff: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AliceBlue),
        verticalArrangement = Arrangement.Center,
    ) {
        Step(stringRes = R.string.choose_region)
        GameRulesComponent()
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp, start = 14.dp, end = 14.dp, bottom = 24.dp),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(6.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                GameOptionCard(
                    icon = R.drawable.africa,
                    stringRes = R.string.central_area,
                    nextStep = { clickableStuff() },
                )
            }

            item {
                GameOptionCard(
                    icon = R.drawable.africa,
                    stringRes = R.string.south_area,
                    nextStep = { clickableStuff() },
                )
            }

            item {
                GameOptionCard(
                    icon = R.drawable.africa,
                    stringRes = R.string.north_area,
                    nextStep = { clickableStuff() },
                )
            }

            item {
                GameOptionCard(
                    icon = R.drawable.africa,
                    stringRes = R.string.east_area,
                    nextStep = { clickableStuff() },
                )
            }

            item {
                GameOptionCard(
                    icon = R.drawable.africa,
                    stringRes = R.string.west_area,
                    nextStep = { clickableStuff() },
                )
            }

            item {
                GameOptionCard(
                    icon = R.drawable.africa,
                    stringRes = R.string.all_area,
                    nextStep = { clickableStuff() },
                )
            }
        }
    }
}

@Composable
private fun GameRulesComponent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
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
            text = "Game Rules",
            fontSize = 24.sp,
            color = AliceBlue,
            textAlign = TextAlign.Center,
        )
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
                GameRuleValueComponent(valueName = "Africa", valueIcon = R.drawable.africa)
            }
        }
    }
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
    valueName: String,
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
            text = valueName,
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
    ChooseRegionComponent(clickableStuff = {})
}

@Preview(showBackground = true)
@Composable
private fun GameRulesComponentPreview() {
    GameRulesComponent()
}

@Preview(showBackground = true, widthDp = 165)
@Composable
private fun GameRuleKeyComponentPreview() {
    GameRuleKeyComponent("Chosen Continent:")
}

@Preview(showBackground = true, widthDp = 165)
@Composable
private fun GameRuleValueComponentPreview() {
    GameRuleValueComponent("Africa", R.drawable.africa)
}
