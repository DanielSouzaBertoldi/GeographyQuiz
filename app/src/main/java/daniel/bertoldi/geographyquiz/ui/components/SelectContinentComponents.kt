package daniel.bertoldi.geographyquiz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import daniel.bertoldi.geographyquiz.Continent
import daniel.bertoldi.geographyquiz.R

@Composable
internal fun SelectContinentComponent(nextStep: (Continent) -> Unit) {
    val deviceWidth = LocalConfiguration.current.screenWidthDp.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Step(stringRes = R.string.choose_continent)
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp, start = 14.dp, end = 14.dp, bottom = 24.dp),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(6.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            itemsIndexed(
                items = Continent.entries.toList(),
                span = { idx, _ ->
                    val span = if (idx == Continent.entries.lastIndex) 2 else 1
                    GridItemSpan(span)
                },
            ) { idx, continent ->
                val modifier = if (idx == Continent.entries.lastIndex) {
                    Modifier.padding(horizontal = deviceWidth / 5)
                } else {
                    Modifier
                }

                GameOptionCard(
                    modifier = modifier,
                    icon = continent.countryIcon,
                    stringRes = continent.countryString,
                    nextStep = { nextStep(continent) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectContinentComponentPreview() {
    SelectContinentComponent(nextStep = {})
}
