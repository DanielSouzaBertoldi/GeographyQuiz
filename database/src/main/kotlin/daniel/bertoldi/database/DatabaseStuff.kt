package daniel.bertoldi.database

import daniel.bertoldi.network.BaseCountryDataResponse
import javax.inject.Inject

class DatabaseStuff @Inject constructor(
    private val countriesDao: CountriesDao,
) : DatabaseInterface {

    override fun getAllCountries() = countriesDao.getAll()

    override fun fetchCountriesCount() = countriesDao.fetchCountriesCount()

    override fun saveCountries(countries: List<CountryEntity>) {
        countriesDao.insertCountries(countries)
    }

    override fun fetchCountriesInContinent(continent: String) =
        countriesDao.fetchCountriesInContinent(continent)
}