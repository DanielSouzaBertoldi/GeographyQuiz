package daniel.bertoldi.geographyquiz.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import daniel.bertoldi.geographyquiz.datasource.CountriesLocalDataSource
import daniel.bertoldi.geographyquiz.datasource.CountriesRemoteDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Duration
import javax.inject.Inject

private const val COUNTRIES_REQUEST_FETCH_TIME = "countries_request_fetch_time"

class CountriesDefaultRepository @Inject constructor(
    private val localDataSource: CountriesLocalDataSource,
    private val remoteDataSource: CountriesRemoteDataSource,
    private val dataStore: DataStore<Preferences>,
) : CountriesRepository {
    override suspend fun getCountries(
        // TODO: pass coroutines scope
    ) {
        if (checkCache()) {
            val countries = remoteDataSource.fetchCountriesApi()
            // localDateSource.saveCountries(countries)
        } else {
            localDataSource.fetchCountriesDb()
        }
    }

    private suspend fun checkCache(): Boolean {
        val storedCacheTime = longPreferencesKey(COUNTRIES_REQUEST_FETCH_TIME)
        val currentTime = System.currentTimeMillis()

        return dataStore.data
            .map {
                val cacheTimes = it[storedCacheTime] ?: 0
                Duration.ofMillis(currentTime - cacheTimes).toDays() > 7
            }
            .first()
    }
}