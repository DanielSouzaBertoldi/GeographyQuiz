package daniel.bertoldi.geographyquiz.domain.mapper

import daniel.bertoldi.database.entities.CountryEntity
import daniel.bertoldi.geographyquiz.domain.model.CountryModel

interface CountryModelToEntityMapper {

    fun mapFrom(countries: List<CountryModel>): List<CountryEntity>
}