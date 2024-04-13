package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.network.BaseCountryDataResponse

interface CountriesRemoteDataSource {

    suspend fun fetchCountriesApi(): List<BaseCountryDataResponse>?
}