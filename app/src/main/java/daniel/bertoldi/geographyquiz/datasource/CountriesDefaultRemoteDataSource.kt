package daniel.bertoldi.geographyquiz.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import daniel.bertoldi.geographyquiz.domain.mapper.BaseCountryDataResponseToModelMapper
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import retrofit2.Retrofit
import javax.inject.Inject

private const val COUNTRIES_REQUEST_FETCH_TIME = "countries_request_fetch_time"

class CountriesDefaultRemoteDataSource @Inject constructor(
    private val countriesApi: CountriesApi,
    private val dataStore: DataStore<Preferences>,
    private val responseToModelMapper: BaseCountryDataResponseToModelMapper,
) : CountriesRemoteDataSource {

    // TODO: return Flow
    override suspend fun fetchCountriesApi(): List<CountryModel> {
        // TODO: Maybe this should be inside its own UseCase
        dataStore.edit { settings ->
            val fetchTime = longPreferencesKey(COUNTRIES_REQUEST_FETCH_TIME)
            settings[fetchTime] = System.currentTimeMillis()
        }
        return responseToModelMapper.mapFrom(countriesApi.getCountries().body()) ?: emptyList()
    }
}