package daniel.bertoldi.geographyquiz.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import daniel.bertoldi.geographyquiz.domain.mapper.BaseCountryDataResponseToModelMapper
import daniel.bertoldi.geographyquiz.factory.BaseCountryDataResponseFactory
import daniel.bertoldi.network.BaseCountryDataResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import retrofit2.Response
import retrofit2.Retrofit

class CountriesDefaultRemoteDataSourceTest {

    private val countriesApi: CountriesApi = mockk()
    private val dataStore: DataStore<Preferences> = mockk()
    private val responseToModelMapper: BaseCountryDataResponseToModelMapper = mockk()
    private val remoteDataSource = CountriesDefaultRemoteDataSource(
        countriesApi = countriesApi,
        dataStore = dataStore, // TODO: fix tests to actually mock this funny guy
        responseToModelMapper = responseToModelMapper,
    )

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
        coEvery { countriesApi.getCountries() } returns getCountriesResponse
        // coEvery { dataStore.updateData(transform = any()) } returns
    }
}