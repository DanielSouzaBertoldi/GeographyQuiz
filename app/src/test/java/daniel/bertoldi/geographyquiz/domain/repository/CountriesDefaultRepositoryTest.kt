package daniel.bertoldi.geographyquiz.domain.repository

import daniel.bertoldi.geographyquiz.datasource.CountriesLocalDataSource
import daniel.bertoldi.geographyquiz.datasource.CountriesRemoteDataSource
import daniel.bertoldi.geographyquiz.datastore.CountriesDataStore
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import daniel.bertoldi.geographyquiz.factory.CountryModelFactory
import daniel.bertoldi.test.utils.randomBoolean
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CountriesDefaultRepositoryTest {

    private val localDataSource: CountriesLocalDataSource = mockk()
    private val remoteDataSource: CountriesRemoteDataSource = mockk()
    private val countriesDataStore: CountriesDataStore = mockk()
    private val repository = CountriesDefaultRepository(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource,
        countriesDataStore = countriesDataStore,
    )

    @Test
    fun getCountries_withCacheGreaterThanSevenDays_assertCountriesFromRemoteReturned() = runTest {
        val remoteResult = CountryModelFactory.makeList()

        prepareScenario(
            remoteDataSourceResult = remoteResult,
            localDataSourceResult = CountryModelFactory.makeList(),
            isCacheGreaterThanSevenDays = true,
        )

        val actual = repository.fetchCountries()

        Assertions.assertEquals(remoteResult, actual)
    }

    @Test
    fun getCountries_withCacheGreaterThanSevenDays_verifyResultSavedInDb() = runTest {
        val remoteResult = CountryModelFactory.makeList()

        prepareScenario(
            remoteDataSourceResult = remoteResult,
            isCacheGreaterThanSevenDays = true,
        )

        repository.fetchCountries()

        coVerify(exactly = 1) {
            localDataSource.saveCountriesInDb(remoteResult)
        }
    }

    @Test
    fun getCountries_withCacheNotGreaterThanSevenDays_assertCountriesFromLocalReturned() = runTest {
        val localResult = CountryModelFactory.makeList()

        prepareScenario(
            remoteDataSourceResult = CountryModelFactory.makeList(),
            localDataSourceResult = localResult,
            isCacheGreaterThanSevenDays = false,
        )

        val actual = repository.fetchCountries()

        Assertions.assertEquals(localResult, actual)
    }

    @Test
    fun getCountries_withCacheNotGreaterThanSevenDays_verifyFetchFromDb() = runTest {
        val localResult = CountryModelFactory.makeList()

        prepareScenario(
            localDataSourceResult = localResult,
            isCacheGreaterThanSevenDays = false,
        )

        repository.fetchCountries()

        coVerify(exactly = 1) {
            localDataSource.fetchCountriesDb()
        }
    }

    private fun prepareScenario(
        remoteDataSourceResult: List<CountryModel> = CountryModelFactory.makeList(),
        localDataSourceResult: List<CountryModel> = CountryModelFactory.makeList(),
        isCacheGreaterThanSevenDays: Boolean = randomBoolean(),
    ) {
        coEvery { remoteDataSource.fetchCountriesApi() } returns remoteDataSourceResult
        coEvery { localDataSource.fetchCountriesDb() } returns localDataSourceResult
        coEvery {
            countriesDataStore.checkCacheGreaterThanSevenDays()
        } returns isCacheGreaterThanSevenDays
        coEvery { localDataSource.saveCountriesInDb(any()) } just runs
    }
}