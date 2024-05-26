package daniel.bertoldi.geographyquiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.bertoldi.geographyquiz.presentation.model.Region
import daniel.bertoldi.geographyquiz.presentation.model.Region.Companion.toRegion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed class AreaScreenState {
    data object Loading : AreaScreenState()
    data class Success(val regionData: Region) : AreaScreenState()
}

@HiltViewModel
class AreaViewModel @Inject constructor() : ViewModel() {
    private val _screenState: MutableStateFlow<AreaScreenState> = MutableStateFlow(AreaScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    fun fetchSubRegions(region: String) {
        val enumRegion = region.toRegion()
        _screenState.value = AreaScreenState.Success(enumRegion)
    }
}