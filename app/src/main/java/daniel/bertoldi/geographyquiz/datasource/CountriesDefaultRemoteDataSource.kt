package daniel.bertoldi.geographyquiz.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import daniel.bertoldi.geographyquiz.datastore.CountriesDataStore
import daniel.bertoldi.geographyquiz.domain.mapper.BaseCountryDataResponseToModelMapper
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import retrofit2.Retrofit
import javax.inject.Inject


class CountriesDefaultRemoteDataSource @Inject constructor(
    private val countriesApi: CountriesApi,
    private val countriesDataStore: CountriesDataStore,
    private val responseToModelMapper: BaseCountryDataResponseToModelMapper,
) : CountriesRemoteDataSource {

    // TODO: return Flow
    override suspend fun fetchCountriesApi(): List<CountryModel> {
        val countriesResult = countriesApi.getCountries()
        return if (countriesResult.isSuccessful) {
            countriesDataStore.saveFetchTime(System.currentTimeMillis())
            responseToModelMapper.mapFrom(countriesResult.body()) ?: emptyList()
        } else {
            emptyList()
        }
    }
}