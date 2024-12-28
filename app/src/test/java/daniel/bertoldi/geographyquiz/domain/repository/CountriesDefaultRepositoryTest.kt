package daniel.bertoldi.geographyquiz.domain.repository

import daniel.bertoldi.geographyquiz.datasource.CountriesLocalDataSource
import daniel.bertoldi.geographyquiz.datasource.CountriesRemoteDataSource
import daniel.bertoldi.geographyquiz.datastore.CountriesDataStore
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import daniel.bertoldi.geographyquiz.factory.CountryModelFactory
import daniel.bertoldi.utilities.test.utils.randomBoolean
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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
    fun getCountries_withInitialFetchFalse_verifyFetchFromRemote() = runTest {
        prepareScenario(didInitialFetch = false)

        repository.fetchCountries()

        coVerify(exactly = 1) { remoteDataSource.fetchCountriesApi() }
    }

    @Test
    fun getCountries_withInitialFetchFalse_verifySaveDataToDbCalled() = runTest {
        prepareScenario(didInitialFetch = false)

        repository.fetchCountries()

        coVerify(exactly = 1) { localDataSource.saveCountriesInDb(any()) }
    }

    @Test
    fun getCountries_withInitialFetchTrue_verifySaveDataToDbCalled() = runTest {
        prepareScenario(didInitialFetch = true, isCacheGreaterThanSevenDays = false)

        repository.fetchCountries()

        coVerify(exactly = 0) { localDataSource.saveCountriesInDb(any()) }
    }

    @Test
    fun getCountries_withInitialFetchTrueAndCacheGreaterThanSevenDays_assertCountriesFromRemote() =
        runTest {
            val remoteResult = CountryModelFactory.makeList()

            prepareScenario(
                remoteDataSourceResult = remoteResult,
                localDataSourceResult = CountryModelFactory.makeList(),
                isCacheGreaterThanSevenDays = true,
                didInitialFetch = true,
            )

            val actual = repository.fetchCountries().first()

            Assertions.assertEquals(remoteResult, actual)
        }

    @Test
    fun getCountries_withInitialFetchAndCacheGreaterThanSevenDays_verifyResultSavedInDb() =
        runTest {
            val remoteResult = CountryModelFactory.makeList()

            prepareScenario(
                remoteDataSourceResult = remoteResult,
                isCacheGreaterThanSevenDays = true,
                didInitialFetch = true,
            )

            repository.fetchCountries()

            coVerify(exactly = 1) {
                localDataSource.saveCountriesInDb(remoteResult)
            }
        }

    @Test
    fun getCountries_withInitialFetchAndCacheNotGreaterThanSevenDays_assertCountriesFromLocal() =
        runTest {
            val localResult = CountryModelFactory.makeList()

            prepareScenario(
                remoteDataSourceResult = CountryModelFactory.makeList(),
                localDataSourceResult = localResult,
                isCacheGreaterThanSevenDays = false,
                didInitialFetch = true,
            )

            val actual = repository.fetchCountries().first()

            Assertions.assertEquals(localResult, actual)
        }

    @Test
    fun getCountries_withRemoteResultEmpty_verifyCountriesFromDbCalled() = runTest {
        prepareScenario(
            remoteDataSourceResult = emptyList(),
            didInitialFetch = true,
        )

        repository.fetchCountries()

        coVerify(exactly = 1) { localDataSource.fetchCountriesDb() }
    }

    @Test
    fun getCountries_withRemoteResultNotEmpty_verifyCountriesFromDbNotCalled() = runTest {
        prepareScenario(
            remoteDataSourceResult = CountryModelFactory.makeList(),
            didInitialFetch = false,
        )

        repository.fetchCountries()

        coVerify(exactly = 0) { localDataSource.fetchCountriesDb() }
    }

    private fun prepareScenario(
        remoteDataSourceResult: List<CountryModel> = CountryModelFactory.makeList(),
        localDataSourceResult: List<CountryModel> = CountryModelFactory.makeList(),
        isCacheGreaterThanSevenDays: Boolean = randomBoolean(),
        didInitialFetch: Boolean = randomBoolean(),
    ) {
        coEvery { remoteDataSource.fetchCountriesApi() } returns remoteDataSourceResult
        coEvery { localDataSource.fetchCountriesDb() } returns flow {
            emit(localDataSourceResult)
        }
        coEvery {
            countriesDataStore.checkCacheGreaterThanSevenDays()
        } returns isCacheGreaterThanSevenDays
        coEvery { countriesDataStore.didInitialFetch() } returns didInitialFetch
        coEvery { localDataSource.saveCountriesInDb(any()) } just runs
    }
}