package daniel.bertoldi.geographyquiz.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import daniel.bertoldi.geographyquiz.Continent
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.ui.theme.AliceBlue
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
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(color = Celadon)
                    .border(Dp.Hairline, Gray)
                    .wrapContentSize(),
                text = stringResource(id = stringRes),
                textAlign = TextAlign.Center,
                color = RichBlack,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
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

@Preview(showBackground = true, widthDp = 165)
@Composable
private fun ContinentCardPreview() {
    GameOptionCard(
        icon = Continent.AFRICA.countryIcon,
        stringRes = Continent.AFRICA.countryString,
        nextStep = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun StepPreview() {
    Step(R.string.choose_continent)
}