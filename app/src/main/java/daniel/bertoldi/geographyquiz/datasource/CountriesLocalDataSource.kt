package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.geographyquiz.domain.model.CountryModel

interface CountriesLocalDataSource {

    suspend fun fetchCountriesDb(): List<CountryModel>

    suspend fun saveCountriesInDb(countries: List<CountryModel>)
}