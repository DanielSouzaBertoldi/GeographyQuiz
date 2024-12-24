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
        return if (shouldFetchFromRemote()) {
            fetchCountriesFromRemote()
        } else {
            fetchCountriesFromLocal()
        }
    }

    override suspend fun getCountriesInSubRegion(
        region: String,
        subRegion: String,
    ) = localDataSource.fetchCountriesInSubRegion(region, subRegion)

    private suspend fun fetchCountriesFromRemote(): Flow<List<CountryModel>> {
        val countries = remoteDataSource.fetchCountriesApi()

        return if (countries.isNotEmpty()) {
            localDataSource.saveCountriesInDb(countries)
            flow { emit(countries) } // TODO: maybe it isn't worth it to have this flow as Flow.
        } else {
            // Since we prepopulate the database, if the request fails
            //  for any reason, we can always use what's in the database.
            fetchCountriesFromLocal()
        }
    }

    private suspend fun fetchCountriesFromLocal()  = localDataSource.fetchCountriesDb()

    private suspend fun shouldFetchFromRemote() =
        !countriesDataStore.didInitialFetch() || countriesDataStore.checkCacheGreaterThanSevenDays()
}