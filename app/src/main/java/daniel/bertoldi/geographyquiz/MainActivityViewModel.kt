package daniel.bertoldi.geographyquiz

import android.util.Log
import androidx.lifecycle.ViewModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import javax.inject.Inject

sealed class ScreenState {
    data object Loading : ScreenState()
    data object Success : ScreenState()
    data object Failed : ScreenState()
    data object SelectContinent : ScreenState()

    data class StartGame(val chosenContinent: Continent) : ScreenState()
}

enum class Continent {
    NORTH_AMERICA,
    SOUTH_AMERICA,
    EUROPE,
    AFRICA,
    ASIA,
    OCEANIA
}

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {
    private val _countries = MutableStateFlow<List<BaseCountryDataResponse>>(emptyList())
    val countries: StateFlow<List<BaseCountryDataResponse>> = _countries.asStateFlow()
    private val _state = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val state: StateFlow<ScreenState> = _state.asStateFlow()
    private val _isAnswerCorrect = MutableStateFlow<Boolean?>(null)
    val isAnswerCorrect = _isAnswerCorrect.asStateFlow()

    fun init() {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://restcountries.com/v3.1/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(APIInterface::class.java)

        retrofit.getCountries().enqueue(
            object : Callback<List<BaseCountryDataResponse>> {
                override fun onResponse(
                    p0: Call<List<BaseCountryDataResponse>>,
                    p1: Response<List<BaseCountryDataResponse>>
                ) {
                    if (p1.isSuccessful) {
                        p1.body()?.let {
                            _countries.value = it
                            _state.value = ScreenState.Success
                        }
                    } else {
                        _state.value = ScreenState.Failed
                    }
                }

                override fun onFailure(p0: Call<List<BaseCountryDataResponse>>, p1: Throwable) {
                    Log.d("FAILED!", p1.stackTraceToString())
                    _state.value = ScreenState.Failed
                }
            }
        )
    }

    interface APIInterface {
        @GET("all")
        fun getCountries(): Call<List<BaseCountryDataResponse>>
    }

    fun startGame() {
        _state.value = ScreenState.SelectContinent
    }

    fun startActualGame(continent: Continent) {
        _state.value = ScreenState.StartGame(continent)
    }

    fun optionClick(answer: BaseCountryDataResponse, clickedOption: BaseCountryDataResponse) {
        if (answer.countryCode == clickedOption.countryCode) {
            _isAnswerCorrect.value = true
        } else {
            _isAnswerCorrect.value = false
        }
    }

    fun drawAgain(answer: BaseCountryDataResponse) {
        _isAnswerCorrect.value = null
        _countries.value = _countries.value.filterNot { it.countryCode == answer.countryCode }
        startActualGame(Continent.SOUTH_AMERICA)
    }
}

@JsonClass(generateAdapter = true)
data class BaseCountryDataResponse(
    val name: CountryNameDataResponse,
    @Json(name = "tld") val topLevelDomains: List<String>?,
    @Json(name = "cca2") val countryCode: String,
    val independent: Boolean?,
    val status: String,
    val unMember: Boolean,
    val idd: InternationalDialResponse,
    val capital: List<String>?,
    @Json(name = "altSpellings") val alternativeSpellings: List<String>,
    val region: String,
    @Json(name = "subregion") val subRegion: String?,
    val languages: Map<String, String>?,
    val translations: SupportedLanguagesResponse,
    val landlocked: Boolean,
    val area: Float,
    @Json(name = "flag") val emojiFlag: String,
    val population: Int,
    val car: CarResponse,
    val timezones: List<String>,
    val continents: List<String>,
    val flags: FlagsResponse,
    val coatOfArms: CoatOfArmsResponse,
    val startOfWeek: String,
)

@JsonClass(generateAdapter = true)
data class CountryNameDataResponse(
    val common: String,
    val official: String,
    val nativeName: Map<String, Yeah>?,
)

@JsonClass(generateAdapter = true)
data class Yeah(
    val common: String,
    val official: String,
)

@JsonClass(generateAdapter = true)
data class InternationalDialResponse(
    val root: String?,
    val suffixes: List<String>?,
)

@JsonClass(generateAdapter = true)
data class SupportedLanguagesResponse(
    val fra: CountryNameDataResponse,
    val ita: CountryNameDataResponse,
    val por: CountryNameDataResponse,
    val spa: CountryNameDataResponse,
)

@JsonClass(generateAdapter = true)
data class CarResponse(
    val signs: List<String>?,
    val side: String,
)

@JsonClass(generateAdapter = true)
data class FlagsResponse(
    val png: String,
    val alt: String?,
)

@JsonClass(generateAdapter = true)
data class CoatOfArmsResponse(
    val png: String?,
)