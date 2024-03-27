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
import kotlin.random.Random

sealed class ScreenState {
    data object Loading : ScreenState()
    data object Success : ScreenState()
    data object Failed : ScreenState()
    data object SelectContinent : ScreenState()
    data object StartGame : ScreenState()
}

enum class Continent(val simpleName: String) {
    NORTH_AMERICA("Americas"),
    SOUTH_AMERICA("Americas"),
    EUROPE("Europe"),
    AFRICA("Africa"),
    ASIA("Asia"),
    OCEANIA("Oceania")
}

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {
    private val countries = MutableStateFlow<List<BaseCountryDataResponse>>(emptyList())
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState = _gameState.asStateFlow()

    init {
        init()
    }

    private fun init() {
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
                            countries.value = it
                            _screenState.value = ScreenState.Success
                        }
                    } else {
                        _screenState.value = ScreenState.Failed
                    }
                }

                override fun onFailure(p0: Call<List<BaseCountryDataResponse>>, p1: Throwable) {
                    Log.d("FAILED!", p1.stackTraceToString())
                    _screenState.value = ScreenState.Failed
                }
            }
        )
    }

    interface APIInterface {
        @GET("all")
        fun getCountries(): Call<List<BaseCountryDataResponse>>
    }

    fun startGame() {
        _screenState.value = ScreenState.SelectContinent
    }

    fun startActualGame(continent: Continent) {
        val filteredCountries = countries.value.filter { it.region == continent.simpleName }
        val drawnCountries = filteredCountries.shuffled().take(5).toMutableList()

        _gameState.value = GameState(
            availableOptions = drawFlagOptions(drawnCountries),
            chosenContinent = continent,
        )
        _screenState.value = ScreenState.StartGame
    }

    fun optionClick(clickedOption: BaseCountryDataResponse) {
        var pointsToAdd = _gameState.value?.score ?: 0
        if (clickedOption.countryCode == gameState.value?.availableOptions?.find { it.isTheCorrectAnswer }?.countryData?.countryCode) {
            pointsToAdd += 20
        } else {
            pointsToAdd -= 20
        }

        _gameState.value = _gameState.value?.copy(
            step = GameStep.OPTION_SELECTED,
            clickedOption = clickedOption,
            score = pointsToAdd,
        )
    }

    fun drawAgain() {
        val filteredCountries = countries.value
            .filterNot { it.countryCode == gameState.value?.availableOptions?.find { it.isTheCorrectAnswer }?.countryData?.countryCode }
            .filter { it.region == gameState.value?.chosenContinent?.simpleName }
        val drawnCountries = filteredCountries.shuffled().take(5).toMutableList()

        _gameState.value = _gameState.value?.copy(
            step = GameStep.CHOOSING_OPTION,
            availableOptions = drawFlagOptions(drawnCountries),
        )
        _screenState.value = ScreenState.StartGame
    }

    private fun drawFlagOptions(countries: MutableList<BaseCountryDataResponse>): List<FlagOption> {
        val flagOptions = mutableListOf<FlagOption>()
        val drawnAnswer = countries.random()

        for (i in 1..5) {
            val randomNumber = Random.nextInt(from = 0, until = countries.size)
            val randomPosition = randomNumber % countries.size
            val randomCountry = countries[randomPosition]

            flagOptions.add(
                FlagOption(
                    isTheCorrectAnswer = randomCountry.countryCode == drawnAnswer.countryCode,
                    countryData = randomCountry,
                )
            )
            countries.removeAt(randomPosition)
        }
        return flagOptions
    }
}

data class GameState(
    val step: GameStep = GameStep.CHOOSING_OPTION,
    val availableOptions: List<FlagOption>,
    val chosenContinent: Continent,
    val clickedOption: BaseCountryDataResponse? = null,
    val score: Int = 0,
)

data class FlagOption(
    val isTheCorrectAnswer: Boolean = false,
    val countryData: BaseCountryDataResponse,
)

enum class GameStep {
    CHOOSING_OPTION,
    OPTION_SELECTED,
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