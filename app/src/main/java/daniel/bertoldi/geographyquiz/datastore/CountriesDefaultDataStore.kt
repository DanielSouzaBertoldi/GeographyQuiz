package daniel.bertoldi.geographyquiz.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Duration
import javax.inject.Inject

private const val COUNTRIES_REQUEST_FETCH_TIME = "countries_request_fetch_time"

class CountriesDefaultDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : CountriesDataStore {
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
            settings[fetchTime] = System.currentTimeMillis()
        }
    }
}