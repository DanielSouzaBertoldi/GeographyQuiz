package daniel.bertoldi.database

import javax.inject.Inject

class CountriesDatabaseStuff @Inject constructor(
    private val countriesDao: CountriesDao,
) : CountriesDatabaseInterface {

    override suspend fun getAllCountries() = countriesDao.getAll()

    override suspend fun fetchCountriesCount() = countriesDao.fetchCountriesCount()

    override suspend fun saveCountries(countries: List<CountryEntity>) =
        countriesDao.insertCountries(countries)

    override suspend fun fetchCountriesInContinent(continent: String) =
        countriesDao.fetchCountriesInContinent(continent)

    override suspend fun fetchAreasInRegion(region: String) =
        countriesDao.fetchAreasInRegion(region)
}