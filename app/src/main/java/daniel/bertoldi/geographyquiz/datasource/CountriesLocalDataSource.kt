package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.database.CountryEntity

interface CountriesLocalDataSource {

    suspend fun fetchCountriesDb(): List<CountryEntity>
}