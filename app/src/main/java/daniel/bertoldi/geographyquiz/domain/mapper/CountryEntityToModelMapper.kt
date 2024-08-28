package daniel.bertoldi.geographyquiz.domain.mapper

import daniel.bertoldi.database.entities.CountryEntity
import daniel.bertoldi.geographyquiz.domain.model.CountryModel

interface CountryEntityToModelMapper {

    fun mapFrom(from: List<CountryEntity>): List<CountryModel>
}