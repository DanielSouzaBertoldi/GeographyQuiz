package daniel.bertoldi.geographyquiz.domain.repository

import daniel.bertoldi.geographyquiz.domain.model.CountryModel

interface CountriesRepository {
    suspend fun getCountries(): List<CountryModel>
}