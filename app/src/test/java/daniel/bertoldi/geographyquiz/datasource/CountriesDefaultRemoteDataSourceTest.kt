package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.geographyquiz.datastore.CountriesDataStore
import daniel.bertoldi.geographyquiz.domain.mapper.BaseCountryDataResponseToModelMapper
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import daniel.bertoldi.geographyquiz.factory.CountryModelFactory
import daniel.bertoldi.network.BaseCountryDataResponse
import daniel.bertoldi.network.BaseCountryDataResponseFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import retrofit2.Response

class CountriesDefaultRemoteDataSourceTest {

    private val countriesApi: CountriesApi = mockk()
    private val countriesDataStore: CountriesDataStore = mockk()
    private val responseToModelMapper: BaseCountryDataResponseToModelMapper = mockk()
    private val remoteDataSource = CountriesDefaultRemoteDataSource(
        countriesApi = countriesApi,
        countriesDataStore = countriesDataStore,
        responseToModelMapper = responseToModelMapper,
    )

    @Test
    fun fetchCountries_successResponse_checkCorrectListOfResponseReturned() = runTest {
        val countryModels = listOf(
            CountryModelFactory.make(),
            CountryModelFactory.make(),
            CountryModelFactory.make(),
        )
        prepareScenario(
            getCountriesResponse = Response.success(
                listOf(BaseCountryDataResponseFactory.make())
            ),
            mapperResult = countryModels,
        )

        val actual = remoteDataSource.fetchCountriesApi()

        Assertions.assertEquals(countryModels, actual)
    }

    @Test
    fun fetchCountries_successResponseBodyNull_assertEmptyReturn() = runTest {
        prepareScenario(
            getCountriesResponse = Response.success(emptyList()),
            mapperResult = emptyList(),
        )

        val actual = remoteDataSource.fetchCountriesApi()

        Assertions.assertEquals(emptyList<CountryModel>(), actual)
    }

    @Test
    fun fetchCountries_successResponse_verifyDataStoreCalled() = runTest {
        prepareScenario(
            getCountriesResponse = Response.success(
                listOf(BaseCountryDataResponseFactory.make())
            ),
            mapperResult = listOf(CountryModelFactory.make()),
        )

        remoteDataSource.fetchCountriesApi()

        coVerify(exactly = 1) {
            countriesDataStore.saveFetchTime(any())
        }
    }

    @Test
    fun fetchCountries_errorResponse_checkResponseIsEmpty() = runTest {
        prepareScenario(
            getCountriesResponse = Response.error(503, "error".toResponseBody())
        )

        val actual = remoteDataSource.fetchCountriesApi()

        Assertions.assertTrue(actual == emptyList<CountryModel>())
    }

    @Test
    fun fetchCountries_errorResponse_verifyDataStoreCalled() = runTest {
        prepareScenario(
            getCountriesResponse = Response.error(503, "error".toResponseBody()),
            mapperResult = listOf(CountryModelFactory.make()),
        )

        remoteDataSource.fetchCountriesApi()

        coVerify(exactly = 0) {
            countriesDataStore.saveFetchTime(any())
        }
    }

    private fun prepareScenario(
        getCountriesResponse: Response<List<BaseCountryDataResponse>> =
            Response.success(listOf(BaseCountryDataResponseFactory.make())),
        mapperResult: List<CountryModel>? = listOf(CountryModelFactory.make()),
    ) {
        coEvery { countriesApi.getCountries() } returns getCountriesResponse
        coEvery { responseToModelMapper.mapFrom(any()) } returns mapperResult

        coEvery { countriesDataStore.saveFetchTime(any()) } just runs
    }
}