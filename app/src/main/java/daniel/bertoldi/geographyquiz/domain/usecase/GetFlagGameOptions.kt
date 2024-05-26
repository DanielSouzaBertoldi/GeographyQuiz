package daniel.bertoldi.geographyquiz.domain.usecase

import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import daniel.bertoldi.geographyquiz.domain.repository.CountriesRepository
import daniel.bertoldi.geographyquiz.presentation.mapper.CountryModelToCountryFlagUiMapper
import daniel.bertoldi.geographyquiz.presentation.model.CountryFlagUi
import daniel.bertoldi.geographyquiz.presentation.model.Region
import daniel.bertoldi.geographyquiz.presentation.model.SubRegion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetFlagGameOptions @Inject constructor(
    private val countriesRepository: CountriesRepository,
    private val countryModelToCountryFlagUiMapper: CountryModelToCountryFlagUiMapper,
): GetFlagGameOptionsUseCase {
    override suspend fun invoke(
        chosenRegion: String,
        chosenSubRegion: String,
    ): Flow<List<CountryFlagUi>> {
        return countriesRepository.getCountries(chosenRegion, chosenSubRegion).transform {
            emit(countryModelToCountryFlagUiMapper.mapFrom(it))
        }
    }
}