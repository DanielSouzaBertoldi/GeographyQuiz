package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.network.BaseCountryDataResponse
import retrofit2.Response
import retrofit2.http.GET

interface CountriesApi {

    @GET("all")
    suspend fun getCountries(): Response<List<BaseCountryDataResponse>>
}