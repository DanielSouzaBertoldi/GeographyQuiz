package daniel.bertoldi.geographyquiz.domain.usecase

import daniel.bertoldi.geographyquiz.domain.repository.CountriesRepository
import javax.inject.Inject

class GetCountriesData @Inject constructor(
    private val countriesRepository: CountriesRepository,
) : GetCountriesDataUseCase {
    override suspend fun invoke() = countriesRepository.getCountries()
}