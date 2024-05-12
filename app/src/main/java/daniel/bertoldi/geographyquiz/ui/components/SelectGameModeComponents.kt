package daniel.bertoldi.geographyquiz.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.Region
import daniel.bertoldi.geographyquiz.SubRegion
import daniel.bertoldi.geographyquiz.presentation.model.GameMode
import daniel.bertoldi.geographyquiz.ui.theme.AliceBlue
import daniel.bertoldi.geographyquiz.ui.theme.RichBlack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGameMode(a: String, b: String) {
    val region = Region.getRegion(a)
    val subRegion = SubRegion.getSubRegion(b)

    var showBottomSheet by remember { mutableStateOf(false) }
    var gameModeHelp: GameMode by remember { mutableStateOf(GameMode.Casual()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = AliceBlue),
        verticalArrangement = Arrangement.Center,
    ) {
        Step(stringRes = R.string.choose_game_mode)
        GameRulesComponent(
            rules = listOf(
                {
                    GameRuleKeyComponent(
                        keyName = R.string.chosen_region,
                        cornerShape = RoundedCornerShape(topStart = 14.dp),
                    )
                },
                {
                    GameRuleValueComponent(
                        valueName = region.regionString,
                        valueIcon = region.regionIcon,
                        cornerShape = RoundedCornerShape(topEnd = 14.dp),
                    )
                },
                {
                    GameRuleKeyComponent(
                        keyName = R.string.chosen_area,
                        cornerShape = RoundedCornerShape(bottomStart = 14.dp),
                    )
                },
                {
                    GameRuleValueComponent(
                        valueName = subRegion.subRegionName,
                        valueIcon = subRegion.subRegionIcon,
                        cornerShape = RoundedCornerShape(bottomEnd = 14.dp),
                    )
                },
            )
        )

        GameModes {
            showBottomSheet = true
            gameModeHelp = it
        }

        if (showBottomSheet) {
            GameModeModal(
                gameModeHelp = gameModeHelp,
                onSheetStateFinishedHiding = { showBottomSheet = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameModeModal(
    gameModeHelp: GameMode,
    onSheetStateFinishedHiding: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    onSheetStateFinishedHiding()
                }
            }
        },
        sheetState = sheetState,
        containerColor = RichBlack,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                width = 78.dp,
                height = 7.dp,
                color = Color.White,
            )
        }
    ) {
        GameModeModalContent(gameModeHelp = gameModeHelp)
    }
}

@Composable
private fun GameModeModalContent(gameModeHelp: GameMode) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .padding(bottom = 42.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = gameModeHelp.name),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 36.sp,
            )
            Image(
                modifier = Modifier
                    .size(60.dp)
                    .padding(start = 20.dp)
                    .graphicsLayer {
                        rotationZ = 30f
                    },
                painter = painterResource(id = gameModeHelp.icon),
                contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = gameModeHelp.description),
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
            color = Color.White
        )
    }
}

@Composable
private fun GameModes(
    onHelpClick: (GameMode) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp),
    ) {
        GameOptionCard(
            modifier = Modifier.width(165.dp),
            icon = R.drawable.casual,
            stringRes = R.string.game_mode_casual,
            nextStep = { /* Add action */ },
            showHelpIcon = true,
            helpIconAction = { onHelpClick(GameMode.Casual()) },
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            GameOptionCard(
                modifier = Modifier.width(165.dp),
                icon = R.drawable.time_attack,
                stringRes = R.string.game_mode_time_attack,
                nextStep = { /* Add action */ },
                showHelpIcon = true,
                helpIconAction = { onHelpClick(GameMode.TimeAttack()) },
            )
            GameOptionCard(
                modifier = Modifier.width(165.dp),
                icon = R.drawable.sudden_death,
                stringRes = R.string.game_mode_sudden_death,
                nextStep = { /* Add action */ },
                showHelpIcon = true,
                helpIconAction = { onHelpClick(GameMode.SuddenDeath()) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectGameModePreview() {
    SelectGameMode(a = "Africa", b = "MIDDLE_AFRICA")
}

@Preview
@Composable
private fun GameModeModalPreview() {
    GameModeModal(
        gameModeHelp = GameMode.Casual(),
        onSheetStateFinishedHiding = {},
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF001A23) // TODO: Can't reference RichBlack directly.
@Composable
private fun GameModeModalContentPreview() {
    GameModeModalContent(GameMode.Casual())
}

@Preview(showBackground = true)
@Composable
private fun GameModesPreview() {
    GameModes(onHelpClick = {})
}
