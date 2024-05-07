package daniel.bertoldi.geographyquiz.domain.usecase

import daniel.bertoldi.geographyquiz.domain.repository.CountriesRepository
import javax.inject.Inject

class GetAreasInRegion @Inject constructor(
    private val countriesRepository: CountriesRepository,
) : GetAreasInRegionUseCase {
    override suspend fun invoke(region: String) = countriesRepository.getAreasInRegion(region)
}