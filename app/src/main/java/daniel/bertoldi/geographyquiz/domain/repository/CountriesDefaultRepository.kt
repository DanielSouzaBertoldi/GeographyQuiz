package daniel.bertoldi.geographyquiz.domain.repository

import daniel.bertoldi.geographyquiz.datasource.CountriesLocalDataSource
import daniel.bertoldi.geographyquiz.datasource.CountriesRemoteDataSource
import daniel.bertoldi.geographyquiz.datastore.CountriesDataStore
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class CountriesDefaultRepository @Inject constructor(
    private val localDataSource: CountriesLocalDataSource,
    private val remoteDataSource: CountriesRemoteDataSource,
    private val countriesDataStore: CountriesDataStore,
) : CountriesRepository {
    override suspend fun fetchCountries(
        // TODO: pass coroutines scope
    ): Flow<List<CountryModel>> {
        return if (checkCache()) {
            val countries = remoteDataSource.fetchCountriesApi()
            localDataSource.saveCountriesInDb(countries)
            flow { emit(countries) } // TODO: dumb stuff
        } else {
            localDataSource.fetchCountriesDb()
        }
    }

    override suspend fun getCountries(
        region: String,
        subRegion: String,
    ): Flow<List<CountryModel>> {
        return localDataSource.fetchCountriesGivenArea(region, subRegion)
    }

    private suspend fun checkCache(): Boolean {
        return countriesDataStore.checkCacheGreaterThanSevenDays()
    }
}