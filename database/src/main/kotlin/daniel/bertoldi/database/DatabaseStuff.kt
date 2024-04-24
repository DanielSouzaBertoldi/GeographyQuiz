package daniel.bertoldi.database

import javax.inject.Inject

class DatabaseStuff @Inject constructor(
    private val countriesDao: CountriesDao,
) : DatabaseInterface {

    override suspend fun getAllCountries() = countriesDao.getAll()

    override suspend fun fetchCountriesCount() = countriesDao.fetchCountriesCount()

    override suspend fun saveCountries(countries: List<CountryEntity>) =
        countriesDao.insertCountries(countries)

    override suspend fun fetchCountriesInContinent(continent: String) =
        countriesDao.fetchCountriesInContinent(continent)
}