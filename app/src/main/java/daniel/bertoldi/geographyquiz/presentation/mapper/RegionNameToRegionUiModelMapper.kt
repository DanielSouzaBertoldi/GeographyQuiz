package daniel.bertoldi.geographyquiz.presentation.mapper

import daniel.bertoldi.geographyquiz.Region
import daniel.bertoldi.geographyquiz.presentation.model.AreaUiModel

interface RegionNameToRegionUiModelMapper {
    fun mapFrom(region: Region): List<AreaUiModel>
}