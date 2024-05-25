package daniel.bertoldi.geographyquiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.bertoldi.geographyquiz.presentation.model.Region
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
        val enumRegion = Region.getRegion(region)
        _screenState.value = AreaScreenState.Success(enumRegion)
    }
}