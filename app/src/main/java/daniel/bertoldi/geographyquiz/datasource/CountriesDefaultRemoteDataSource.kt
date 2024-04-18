package daniel.bertoldi.geographyquiz.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import daniel.bertoldi.network.BaseCountryDataResponse
import retrofit2.Retrofit
import javax.inject.Inject

private const val COUNTRIES_REQUEST_FETCH_TIME = "countries_request_fetch_time"

class CountriesDefaultRemoteDataSource @Inject constructor(
    private val retrofit: Retrofit,
    private val countriesApi: CountriesApi, // TODO: how to only use countriesApi directly?
    private val dataStore: DataStore<Preferences>,
) : CountriesRemoteDataSource {

    // TODO: return Flow
    override suspend fun fetchCountriesApi(): List<BaseCountryDataResponse>? {
        // TODO: so I won't have to do this?
        val countriesRetrofit = retrofit.create(CountriesApi::class.java)
        // TODO: Maybe this should be inside its own usecase
        dataStore.edit { settings ->
            val fetchTime = longPreferencesKey(COUNTRIES_REQUEST_FETCH_TIME)
            settings[fetchTime] = System.currentTimeMillis()
        }
        return countriesRetrofit.getCountries().body()
    }
}