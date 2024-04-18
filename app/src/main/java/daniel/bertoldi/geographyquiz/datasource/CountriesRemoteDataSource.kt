package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.geographyquiz.domain.model.CountryModel

interface CountriesRemoteDataSource {

    suspend fun fetchCountriesApi(): List<CountryModel>?
}