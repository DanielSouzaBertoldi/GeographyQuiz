package daniel.bertoldi.geographyquiz.domain.repository

import daniel.bertoldi.geographyquiz.datasource.CountriesLocalDataSource
import daniel.bertoldi.geographyquiz.datasource.CountriesRemoteDataSource
import daniel.bertoldi.geographyquiz.datastore.CountriesDataStore
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import javax.inject.Inject


class CountriesDefaultRepository @Inject constructor(
    private val localDataSource: CountriesLocalDataSource,
    private val remoteDataSource: CountriesRemoteDataSource,
    private val countriesDataStore: CountriesDataStore,
) : CountriesRepository {
    override suspend fun getCountries(
        // TODO: pass coroutines scope
    ): List<CountryModel> {
        return if (checkCache()) {
            val countries = remoteDataSource.fetchCountriesApi()
            localDataSource.saveCountriesInDb(countries)
            countries
        } else {
            localDataSource.fetchCountriesDb()
        }
    }

    private suspend fun checkCache(): Boolean {
        return countriesDataStore.checkCacheGreaterThanSevenDays()
    }
}