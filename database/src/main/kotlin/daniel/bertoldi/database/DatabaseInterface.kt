package daniel.bertoldi.database

import daniel.bertoldi.network.BaseCountryDataResponse

interface DatabaseInterface {

    fun getDb(): List<CountryEntity>
    fun fetchCountriesCount(): Int
    fun saveCountries(countries: List<BaseCountryDataResponse>)
    fun fetchCountriesInContinent(continent: String): List<CountryEntity>
}