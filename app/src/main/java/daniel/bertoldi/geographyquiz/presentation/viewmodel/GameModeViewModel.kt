package daniel.bertoldi.geographyquiz.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.bertoldi.geographyquiz.presentation.model.Region
import daniel.bertoldi.geographyquiz.presentation.model.SubRegion
import daniel.bertoldi.geographyquiz.presentation.model.GameMode
import daniel.bertoldi.geographyquiz.presentation.model.Region.Companion.toRegion
import daniel.bertoldi.geographyquiz.presentation.model.SubRegion.Companion.toSubRegion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/*
 * Game mode idea: have the flag b&w, upside-down, inverted, whatever.
 * Have the user try to guess the correct country with the least number of
 * hints (maybe max 3 hints?)
 */
sealed class GameModeScreenState {
    data object Loading : GameModeScreenState()

    data class ChoosingGameMode(
        val region: Region,
        val subRegion: SubRegion,
    ) : GameModeScreenState()

    data class ConfirmSelection(
        val region: Region,
        val subRegion: SubRegion,
        val gameMode: GameMode,
    ) : GameModeScreenState()
}

@HiltViewModel
class GameModeViewModel @Inject constructor() : ViewModel() {
    private val _screenState: MutableStateFlow<GameModeScreenState> =
        MutableStateFlow(GameModeScreenState.Loading)
    val screenState = _screenState.asStateFlow()
    var chosenRegion: Region? = null
    var chosenSubRegion: SubRegion? = null
    var chosenGameMode: GameMode? = null

    fun fetchArguments(arguments: Bundle?) {
        val region = arguments?.getString("region").orEmpty()
        val subRegion = arguments?.getString("subRegion").orEmpty()

        _screenState.value = GameModeScreenState.ChoosingGameMode(
            region = region.toRegion(),
            subRegion = subRegion.toSubRegion(),
        )
    }

    fun confirmSelection(gameMode: GameMode, region: Region, subRegion: SubRegion) {
        chosenRegion = region
        chosenSubRegion = subRegion
        chosenGameMode = gameMode
        _screenState.value = GameModeScreenState.ConfirmSelection(
            region = region,
            subRegion = subRegion,
            gameMode = gameMode,
        )
    }
}