package daniel.bertoldi.geographyquiz.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.bertoldi.geographyquiz.Region
import daniel.bertoldi.geographyquiz.SubRegion
import daniel.bertoldi.geographyquiz.presentation.model.GameMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

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

    fun fetchArguments(arguments: Bundle?) {
        val region = arguments?.getString("region").orEmpty()
        val subRegion = arguments?.getString("subRegion").orEmpty()

        _screenState.value = GameModeScreenState.ChoosingGameMode(
            region = Region.getRegion(region),
            subRegion = SubRegion.getSubRegion(subRegion),
        )
    }

    fun confirmSelection(gameMode: GameMode, region: Region, subRegion: SubRegion) {
        _screenState.value = GameModeScreenState.ConfirmSelection(
            region = region,
            subRegion = subRegion,
            gameMode = gameMode,
        )
    }
}