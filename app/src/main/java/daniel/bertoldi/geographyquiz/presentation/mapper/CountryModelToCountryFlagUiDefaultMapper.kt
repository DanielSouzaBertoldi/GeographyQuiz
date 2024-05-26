package daniel.bertoldi.geographyquiz.presentation.mapper

import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import daniel.bertoldi.geographyquiz.presentation.model.CountryFlagUi
import javax.inject.Inject

class CountryModelToCountryFlagUiDefaultMapper @Inject constructor() :
    CountryModelToCountryFlagUiMapper {
    override fun mapFrom(countries: List<CountryModel>) = countries.map {
        CountryFlagUi(
            countryCode = it.countryCode,
            countryName = it.name.common,
            flagUrl = it.flagPng,
        )
    }
}