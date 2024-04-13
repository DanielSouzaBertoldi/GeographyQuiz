package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.network.BaseCountryDataResponse
import retrofit2.Retrofit
import javax.inject.Inject

class CountriesDefaultRemoteDataSource @Inject constructor(
    private val retrofit: Retrofit,
    private val countriesApi: CountriesApi, // TODO: how to only use countriesApi directly?
) : CountriesRemoteDataSource {
    override suspend fun fetchCountriesApi(): List<BaseCountryDataResponse>? {
        // TODO: so I won't have to do this?
        val countriesRetrofit = retrofit.create(CountriesApi::class.java)
        return countriesRetrofit.getCountries().body()
    }
}