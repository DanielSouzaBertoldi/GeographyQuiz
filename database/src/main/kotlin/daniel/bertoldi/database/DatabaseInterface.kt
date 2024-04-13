package daniel.bertoldi.database

import daniel.bertoldi.network.BaseCountryDataResponse

interface DatabaseInterface {

    fun getAllCountries(): List<CountryEntity>
    fun fetchCountriesCount(): Int
    fun saveCountries(countries: List<BaseCountryDataResponse>)
    fun fetchCountriesInContinent(continent: String): List<CountryEntity>
}