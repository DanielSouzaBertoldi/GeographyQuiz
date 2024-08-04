package daniel.bertoldi.geographyquiz.domain.usecase

import daniel.bertoldi.geographyquiz.domain.repository.CountriesRepository
import daniel.bertoldi.geographyquiz.factory.CountryModelFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetCountriesDataTest {

    private val countriesRepository: CountriesRepository = mockk()
    private val useCase = GetCountriesData(countriesRepository)

    @Test
    fun invoke_verifyRepositoryCalled() = runTest {
        coEvery { countriesRepository.fetchCountries() } returns flow {
            emit(CountryModelFactory.makeList())
        }

        useCase()

        coVerify(exactly = 1) { countriesRepository.fetchCountries() }
    }

}