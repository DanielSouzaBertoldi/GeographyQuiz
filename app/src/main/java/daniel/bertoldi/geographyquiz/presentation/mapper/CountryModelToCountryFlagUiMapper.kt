package daniel.bertoldi.geographyquiz.presentation.mapper

import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import daniel.bertoldi.geographyquiz.presentation.model.CountryFlagUi

interface CountryModelToCountryFlagUiMapper {
    fun mapFrom(countries: List<CountryModel>): List<CountryFlagUi>
}