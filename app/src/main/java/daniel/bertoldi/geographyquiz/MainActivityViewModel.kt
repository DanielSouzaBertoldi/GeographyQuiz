package daniel.bertoldi.geographyquiz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.bertoldi.database.DatabaseInterface
import daniel.bertoldi.network.BaseCountryDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import javax.inject.Inject

sealed class MainScreenState {
    data object Loading : MainScreenState()
    data object Success : MainScreenState()
    data object Failed : MainScreenState()
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val databaseStuff: DatabaseInterface,
    private val retrofit: Retrofit,
) : ViewModel() {
    private val _mainScreenState = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val mainScreenState: StateFlow<MainScreenState> = _mainScreenState.asStateFlow()

    init { init() }

    private fun init() {
        val retrofit = retrofit.create(RestCountriesApiInterface::class.java)

        retrofit.getCountries().enqueue(
            object : Callback<List<BaseCountryDataResponse>> {
                override fun onResponse(
                    p0: Call<List<BaseCountryDataResponse>>,
                    p1: Response<List<BaseCountryDataResponse>>
                ) {
                    if (p1.isSuccessful) {
                        p1.body()?.let {
                            viewModelScope.launch(
                                Dispatchers.IO
                            ) {
                                if (it.size > databaseStuff.fetchCountriesCount())
                                    databaseStuff.saveCountries(it)
                            }
                            _mainScreenState.value = MainScreenState.Success
                        }
                    } else {
                        if (databaseStuff.fetchCountriesCount() == 0)
                            _mainScreenState.value = MainScreenState.Failed
                        else
                            _mainScreenState.value = MainScreenState.Success
                    }
                }

                override fun onFailure(p0: Call<List<BaseCountryDataResponse>>, p1: Throwable) {
                    Log.d("FAILED!", p1.stackTraceToString())
                    _mainScreenState.value = MainScreenState.Failed
                }
            }
        )
    }

    interface RestCountriesApiInterface {
        @GET("all")
        fun getCountries(): Call<List<BaseCountryDataResponse>>
    }
}