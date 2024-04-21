package daniel.bertoldi.geographyquiz.domain.usecase

import daniel.bertoldi.geographyquiz.domain.model.CountryModel

interface GetCountriesDataUseCase {

    suspend operator fun invoke(): List<CountryModel>
}