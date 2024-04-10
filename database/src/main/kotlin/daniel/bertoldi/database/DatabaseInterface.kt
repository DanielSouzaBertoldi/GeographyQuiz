package daniel.bertoldi.database

import daniel.bertoldi.network.BaseCountryDataResponse

interface DatabaseInterface {

    fun getDb(): List<CountryEntity>

    fun saveCountry(country: BaseCountryDataResponse)
    fun saveCountries(countries: List<BaseCountryDataResponse>)
}