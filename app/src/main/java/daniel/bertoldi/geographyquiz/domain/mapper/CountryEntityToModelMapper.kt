package daniel.bertoldi.geographyquiz.domain.mapper

import daniel.bertoldi.database.CountryEntity
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import kotlinx.coroutines.flow.Flow

interface CountryEntityToModelMapper {

    fun mapFrom(from: List<CountryEntity>): List<CountryModel>
}