package daniel.bertoldi.geographyquiz.presentation.mapper

import daniel.bertoldi.geographyquiz.R
import daniel.bertoldi.geographyquiz.Region
import daniel.bertoldi.geographyquiz.presentation.model.AreaUiModel
import javax.inject.Inject

class RegionNameToRegionUiModelDefaultMapper @Inject constructor() : RegionNameToRegionUiModelMapper {

    override fun mapFrom(region: Region) = region.subRegions.map {
        AreaUiModel(
            name = it.subRegionName,
            icon = it.subRegionIcon,
        )
    }
}