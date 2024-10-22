package daniel.bertoldi.geographyquiz.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.presentation.model.Region
import daniel.bertoldi.geographyquiz.presentation.viewmodel.AreaScreenState
import daniel.bertoldi.geographyquiz.presentation.ui.theme.AliceBlue

@Composable
internal fun ChooseAreaComponent(
    screenState: AreaScreenState,
    nextStep: (String) -> Unit,
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
            TableComponent(
                tableHeaderText = R.string.game_rules,
                shouldAnimateHeader = true,
                tableContent = listOf(
                    TableData(
                        TableKey(name = stringResource(id = R.string.chosen_region)),
                        TableValue(
                            name = stringResource(id = screenState.regionData.regionString),
                            icon = screenState.regionData.regionIcon,
                        ),
                    )
                ),
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

@Preview(showBackground = true)
@Composable
private fun ChooseAreaComponentPreview() {
    ChooseAreaComponent(
        nextStep = {},
        screenState = AreaScreenState.Success(Region.ASIA)
    )
}
