package daniel.bertoldi.geographyquiz

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


enum class Continent(val simpleName: String) {
    NORTH_AMERICA("Americas"),
    SOUTH_AMERICA("Americas"),
    EUROPE("Europe"),
    AFRICA("Africa"),
    ASIA("Asia"),
    OCEANIA("Oceania")
}


@HiltViewModel
class ContinentScreenViewModel @Inject constructor() : ViewModel() {

//    fun startActualGame(continent: Continent) {
//        val filteredCountries = countries.value.filter { it.region == continent.simpleName }
//        val drawnCountries = filteredCountries.shuffled().take(5).toMutableList()
//
//        _gameState.value = GameState(
//            availableOptions = drawFlagOptions(drawnCountries),
//            chosenContinent = continent,
//        )
//        _mainScreenState.value = MainScreenState.StartGame
//    }
}