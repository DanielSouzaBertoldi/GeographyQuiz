package daniel.bertoldi.geographyquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.bertoldi.geographyquiz.domain.usecase.GetCountriesDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MainScreenState {
    data object Loading : MainScreenState()
    data object Success : MainScreenState()
    data object Failed : MainScreenState()
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getCountriesData: GetCountriesDataUseCase,
) : ViewModel() {
    private val _mainScreenState = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val mainScreenState: StateFlow<MainScreenState> = _mainScreenState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO: how to fetch possible error scenarios?
            getCountriesData()
            _mainScreenState.value = MainScreenState.Success
        }
    }
}