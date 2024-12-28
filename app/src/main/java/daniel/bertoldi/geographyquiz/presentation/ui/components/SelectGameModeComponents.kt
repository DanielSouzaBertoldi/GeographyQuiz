package daniel.bertoldi.geographyquiz.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.presentation.model.GameMode
import daniel.bertoldi.geographyquiz.presentation.model.Region
import daniel.bertoldi.geographyquiz.presentation.model.SubRegion
import daniel.bertoldi.utilities.design.tokens.AliceBlue
import daniel.bertoldi.utilities.design.tokens.BrunswickGreen
import daniel.bertoldi.utilities.design.tokens.Celadon
import daniel.bertoldi.utilities.design.tokens.RichBlack
import daniel.bertoldi.geographyquiz.presentation.viewmodel.GameModeScreenState
import kotlinx.coroutines.launch

const val ACTION_BUTTON_TEST_TAG ="ActionButton"

@Composable
fun SelectGameMode(
    screenState: GameModeScreenState,
    onGameModeClick: (GameMode, Region, SubRegion) -> Unit,
    onPlayGameClick: (region: String, subRegion: String, GameMode) -> Unit,
    onUndoChoicesClick: () -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = AliceBlue)
            .padding(top = 40.dp)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (screenState) {
            is GameModeScreenState.Loading -> {
                LoadingComponent()
            }

            is GameModeScreenState.ChoosingGameMode -> {
                ChooseGameModeComponent(
                    region = screenState.region,
                    subRegion = screenState.subRegion,
                    onGameModeClick = onGameModeClick,
                )
            }

            is GameModeScreenState.ConfirmSelection -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    TableComponent(
                        tableHeaderText = R.string.game_rules,
                        tableContent = listOf(
                            TableData(
                                TableKey(name = stringResource(id = R.string.chosen_region)),
                                TableValue(
                                    name = stringResource(id = screenState.region.regionString),
                                    icon = screenState.region.regionIcon,
                                )
                            ),
                            TableData(
                                TableKey(name = stringResource(id = R.string.chosen_area)),
                                TableValue(
                                    name = stringResource(screenState.subRegion.subRegionName),
                                    icon = screenState.subRegion.subRegionIcon,
                                )
                            ),
                            TableData(
                                TableKey(name = stringResource(id = R.string.chosen_game_mode)),
                                TableValue(
                                    name = stringResource(screenState.gameMode.title),
                                    icon = screenState.gameMode.icon,
                                )
                            ),
                        ),
                    )
                    ActionButton(
                        modifier = Modifier.padding(top = 60.dp),
                        text = R.string.play,
                        action = {
                            onPlayGameClick(
                                screenState.region.simpleName,
                                // TODO: decide if this is worth it.
                                context.getString(screenState.subRegion.subRegionName),
                                screenState.gameMode,
                            )
                        },
                        textColor = RichBlack,
                        backgroundColor = Celadon,
                        fontWeight = FontWeight.Bold,
                        textSize = 40.sp,
                    )
                    ActionButton(
                        modifier = Modifier.padding(top = 20.dp),
                        text = R.string.undo_choices,
                        action = { onUndoChoicesClick() },
                        textColor = AliceBlue,
                        backgroundColor = BrunswickGreen,
                        fontWeight = FontWeight.Light,
                        textSize = 32.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun ChooseGameModeComponent(
    region: Region,
    subRegion: SubRegion,
    onGameModeClick: (GameMode, Region, SubRegion) -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var gameModeHelp: GameMode by remember { mutableStateOf(GameMode.Casual()) }

    Step(stringRes = R.string.choose_game_mode)
    TableComponent(
        tableHeaderText = R.string.game_rules,
        tableContent = listOf(
            TableData(
                TableKey(name = stringResource(id = R.string.chosen_region)),
                TableValue(
                    name = stringResource(region.regionString),
                    icon = region.regionIcon,
                )
            ),
            TableData(
                TableKey(name = stringResource(id = R.string.chosen_area)),
                TableValue(
                    name = stringResource(subRegion.subRegionName),
                    icon = subRegion.subRegionIcon,
                )
            ),
        ),
    )

    GameModes(
        onHelpClick = {
            showBottomSheet = true
            gameModeHelp = it
        },
        onGameModeClick = { onGameModeClick(it, region, subRegion) },
    )

    if (showBottomSheet) {
        GameModeModal(
            gameModeHelp = gameModeHelp,
            onSheetStateFinishedHiding = { showBottomSheet = false }
        )
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
                text = stringResource(id = gameModeHelp.title),
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
    onGameModeClick: (GameMode) -> Unit,
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
            nextStep = { onGameModeClick(GameMode.Casual()) },
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
                nextStep = { onGameModeClick(GameMode.TimeAttack()) },
                showHelpIcon = true,
                helpIconAction = { onHelpClick(GameMode.TimeAttack()) },
            )
            GameOptionCard(
                modifier = Modifier.width(165.dp),
                icon = R.drawable.sudden_death,
                stringRes = R.string.game_mode_sudden_death,
                nextStep = { onGameModeClick(GameMode.SuddenDeath()) },
                showHelpIcon = true,
                helpIconAction = { onHelpClick(GameMode.SuddenDeath()) },
            )
        }
    }
}

@Composable
internal fun ActionButton(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    action: () -> Unit,
    textColor: Color,
    backgroundColor: Color,
    fontWeight: FontWeight,
    textSize: TextUnit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(color = backgroundColor)
            .border(width = Dp.Hairline, color = Color.Black, shape = RoundedCornerShape(24.dp))
            .clickable { action() }
            .padding(vertical = 10.dp)
            .testTag(ACTION_BUTTON_TEST_TAG),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = text),
            color = textColor,
            fontSize = textSize,
            fontWeight = fontWeight,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectGameModePreview() {
    SelectGameMode(
        screenState = GameModeScreenState.ChoosingGameMode(
            region = Region.AFRICA,
            subRegion = SubRegion.EASTERN_AFRICA,
        ),
        onGameModeClick = { _, _, _ -> },
        onPlayGameClick = { _, _, _ -> },
        onUndoChoicesClick = { },
    )
}

@Preview(showBackground = true)
@Composable
private fun SelectConfirmChoicesPreview() {
    SelectGameMode(
        screenState = GameModeScreenState.ConfirmSelection(
            region = Region.AFRICA,
            subRegion = SubRegion.EASTERN_AFRICA,
            gameMode = GameMode.Casual(),
        ),
        onGameModeClick = { _, _, _ -> },
        onPlayGameClick = { _, _, _ -> },
        onUndoChoicesClick = { },
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF001A23 // TODO: Can't reference RichBlack directly, which sucks.
)
@Composable
private fun GameModeModalContentPreview() {
    GameModeModalContent(GameMode.Casual())
}

@Preview(showBackground = true)
@Composable
private fun GameModesPreview() {
    GameModes(
        onHelpClick = {},
        onGameModeClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun ActionButtonPlayPreview() {
    ActionButton(
        text = R.string.play,
        action = {},
        textColor = RichBlack,
        backgroundColor = Celadon,
        fontWeight = FontWeight.Bold,
        textSize = 40.sp,
    )
}

@Preview(showBackground = true)
@Composable
private fun ActionButtonUndoChoicesPreview() {
    ActionButton(
        text = R.string.undo_choices,
        action = {},
        textColor = AliceBlue,
        backgroundColor = BrunswickGreen,
        fontWeight = FontWeight.Light,
        textSize = 32.sp,
    )
}
