package daniel.bertoldi.geographyquiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.bertoldi.geographyquiz.Region
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
        viewModelScope.launch(Dispatchers.IO) {
            val enumRegion = Region.getRegion(region)
            _screenState.value = AreaScreenState.Success(enumRegion)
        }
    }
}