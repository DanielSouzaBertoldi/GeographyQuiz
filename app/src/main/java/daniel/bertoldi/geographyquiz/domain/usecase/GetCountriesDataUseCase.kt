package daniel.bertoldi.geographyquiz.domain.usecase

import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import kotlinx.coroutines.flow.Flow

interface GetCountriesDataUseCase {

    suspend operator fun invoke(): Flow<List<CountryModel>>
}