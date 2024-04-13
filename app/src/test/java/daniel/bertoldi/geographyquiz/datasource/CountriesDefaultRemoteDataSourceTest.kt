package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.geographyquiz.factory.BaseCountryDataResponseFactory
import daniel.bertoldi.network.BaseCountryDataResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import retrofit2.Response
import retrofit2.Retrofit

class CountriesDefaultRemoteDataSourceTest {

    private val retrofit: Retrofit = mockk()
    private val countriesApi: CountriesApi = mockk()
    private val remoteDataSource = CountriesDefaultRemoteDataSource(retrofit, countriesApi)

    @Test
    fun fetchCountries_successResponse_checkCorrectListOfResponseReturned() = runTest {
        val responseList = listOf(
            BaseCountryDataResponseFactory.make(),
            BaseCountryDataResponseFactory.make(),
            BaseCountryDataResponseFactory.make(),
        )
        prepareScenario(
            getCountriesResponse = Response.success(responseList)
        )

        val actual = remoteDataSource.fetchCountriesApi()

        Assertions.assertEquals(responseList, actual)
    }

    @Test
    fun fetchCountries_errorResponse_checkResponseIsNull() = runTest {
        prepareScenario(
            getCountriesResponse = Response.error(503, "error".toResponseBody())
        )

        val actual = remoteDataSource.fetchCountriesApi()

        Assertions.assertNull(actual)
    }

    private fun prepareScenario(
        getCountriesResponse: Response<List<BaseCountryDataResponse>> = Response.success(emptyList())
    ) {
        coEvery {
            // TODO: this seems sketchy.
            retrofit.create(CountriesApi::class.java).getCountries()
        } returns getCountriesResponse
    }
}