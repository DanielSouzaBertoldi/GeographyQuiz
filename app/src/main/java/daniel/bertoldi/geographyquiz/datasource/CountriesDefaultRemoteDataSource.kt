package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.geographyquiz.datastore.CountriesDataStore
import daniel.bertoldi.geographyquiz.domain.mapper.BaseCountryDataResponseToModelMapper
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CountriesDefaultRemoteDataSource @Inject constructor(
    private val countriesApi: CountriesApi,
    private val countriesDataStore: CountriesDataStore,
    private val responseToModelMapper: BaseCountryDataResponseToModelMapper,
) : CountriesRemoteDataSource {

    // TODO: return Flow
    override suspend fun fetchCountriesApi(): List<CountryModel> {
        try {
            // TODO: error -> CountriesApi -> kotlin.net.SocketTimeoutException: failed to connect to restcountries.com/161.35.252.68 (port 443) from /192.168.0.170 (port 47938) after 10000ms
            return withContext(Dispatchers.IO) {
                val countriesResult = countriesApi.getCountries()
                if (countriesResult.isSuccessful) {
                    countriesDataStore.saveFetchTime(System.currentTimeMillis())
                    responseToModelMapper.mapFrom(countriesResult.body()) ?: emptyList()
                } else {
                    emptyList()
                }
            }
        } catch (e: Exception) {
            println("CountriesApi -> $e")
            return emptyList()
        }
    }
}