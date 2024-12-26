package daniel.bertoldi.geographyquiz.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Duration
import javax.inject.Inject

private const val COUNTRIES_REQUEST_FETCH_TIME = "countries_request_fetch_time"
private const val DID_INITIAL_FETCH = "initial_fetch_done"

class CountriesDefaultDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : CountriesDataStore {
    override suspend fun didInitialFetch(): Boolean {
        val didInitialFetch = booleanPreferencesKey(DID_INITIAL_FETCH)

        return dataStore.data.first()[didInitialFetch] ?: false
    }

    override suspend fun checkCacheGreaterThanSevenDays(): Boolean {
        val storedCacheTime = longPreferencesKey(COUNTRIES_REQUEST_FETCH_TIME)
        val currentTime = System.currentTimeMillis()

        return dataStore.data
            .map {
                val cacheTimes = it[storedCacheTime] ?: 8
                Duration.ofMillis(currentTime - cacheTimes).toDays() > 7
            }
            .first()
    }

    override suspend fun saveFetchTime(currentTime: Long) {
        dataStore.edit { settings ->
            val fetchTime = longPreferencesKey(COUNTRIES_REQUEST_FETCH_TIME)
            val didInitialFetch = booleanPreferencesKey(DID_INITIAL_FETCH)
            settings[fetchTime] = System.currentTimeMillis()
            settings[didInitialFetch] = true
        }
    }
}