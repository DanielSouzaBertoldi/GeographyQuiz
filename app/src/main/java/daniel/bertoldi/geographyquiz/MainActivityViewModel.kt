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
import kotlin.random.Random

sealed class MainScreenState {
    data object Loading : MainScreenState()
    data object Success : MainScreenState()
    data object Failed : MainScreenState()
    data object SelectContinent : MainScreenState()
    data object StartGame : MainScreenState()
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val databaseStuff: DatabaseInterface,
    private val retrofit: Retrofit,
) : ViewModel() {
    private val countries = MutableStateFlow<List<BaseCountryDataResponse>>(emptyList())
    private val _mainScreenState = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val mainScreenState: StateFlow<MainScreenState> = _mainScreenState.asStateFlow()
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState = _gameState.asStateFlow()

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
                                databaseStuff.saveCountries(it)
                            }
                            countries.value = it
                            _mainScreenState.value = MainScreenState.Success
                        }
                    } else {
                        _mainScreenState.value = MainScreenState.Failed
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
        _mainScreenState.value = MainScreenState.StartGame
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