package daniel.bertoldi.geographyquiz.domain.repository

import daniel.bertoldi.database.CountryEntity
import daniel.bertoldi.geographyquiz.domain.model.CountryModel

interface CountryModelToEntityMapper {

    fun mapFrom(countries: List<CountryModel>): List<CountryEntity>
}