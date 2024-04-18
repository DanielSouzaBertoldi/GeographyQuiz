package daniel.bertoldi.geographyquiz.domain.mapper

import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import daniel.bertoldi.network.BaseCountryDataResponse

interface BaseCountryDataResponseToModelMapper {
    fun mapFrom(from: List<BaseCountryDataResponse>?): List<CountryModel>?
}