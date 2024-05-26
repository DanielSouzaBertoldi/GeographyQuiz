package daniel.bertoldi.geographyquiz.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import daniel.bertoldi.geographyquiz.R

enum class Region(
    val simpleName: String,
    @StringRes val regionString: Int,
    @DrawableRes val regionIcon: Int,
    val subRegions: List<SubRegion> = emptyList(),
) {
    AFRICA(
        simpleName = "Africa",
        regionString = R.string.africa,
        regionIcon = R.drawable.africa,
        subRegions = listOf(
            SubRegion.ALL_AFRICA,
            SubRegion.EASTERN_AFRICA,
            SubRegion.WESTERN_AFRICA,
            SubRegion.MIDDLE_AFRICA,
            SubRegion.NORTHERN_AFRICA,
            SubRegion.SOUTHERN_AFRICA
        )
    ),
    AMERICAS(
        simpleName = "Americas",
        regionString = R.string.americas,
        regionIcon = R.drawable.americas,
        subRegions = listOf(
            SubRegion.ALL_AMERICA,
            SubRegion.NORTH_AMERICA,
            SubRegion.SOUTH_AMERICA,
            SubRegion.CARIBBEAN,
            SubRegion.CENTRAL_AMERICA,
        )
    ),
    ASIA(
        simpleName = "Asia",
        regionString = R.string.asia,
        regionIcon = R.drawable.asia,
        subRegions = listOf(
            SubRegion.ALL_ASIA,
            SubRegion.WESTERN_ASIA,
            SubRegion.SOUTH_EASTERN_ASIA,
            SubRegion.SOUTHERN_ASIA,
            SubRegion.CENTRAL_ASIA,
            SubRegion.EASTERN_ASIA,
        )
    ),
    EUROPE(
        simpleName = "Europe",
        regionString = R.string.europe,
        regionIcon = R.drawable.europe,
        subRegions = listOf(
            SubRegion.ALL_EUROPE,
            SubRegion.EASTERN_EUROPE,
            SubRegion.SOUTH_EASTERN_EUROPE,
            SubRegion.NORTHERN_EUROPE,
            SubRegion.SOUTHERN_EUROPE,
            SubRegion.CENTRAL_EUROPE,
            SubRegion.WESTERN_EUROPE,
        )
    ),
    OCEANIA(
        simpleName = "Oceania",
        regionString = R.string.oceania,
        regionIcon = R.drawable.australia,
        subRegions = listOf(
            SubRegion.ALL_OCEANIA,
            SubRegion.MICRONESIA,
            SubRegion.POLYNESIA,
            SubRegion.MELANESIA,
            SubRegion.AUS_NEW,
        )
    );

    companion object {
        fun String.toRegion() = entries.first {
            it.simpleName.equals(this, ignoreCase = true)
        }
    }
}

enum class SubRegion(
    @StringRes val subRegionName: Int,
    @DrawableRes val subRegionIcon: Int,
) {
    ALL_AFRICA(R.string.all_area, R.drawable.all_africa),
    EASTERN_AFRICA(R.string.eastern, R.drawable.eastern_africa),
    WESTERN_AFRICA(R.string.western, R.drawable.western_africa),
    MIDDLE_AFRICA(R.string.middle, R.drawable.western_africa),
    NORTHERN_AFRICA(R.string.northern, R.drawable.northern_africa),
    SOUTHERN_AFRICA(R.string.southern, R.drawable.southern_africa),

    ALL_AMERICA(R.string.all_area, R.drawable.all_americas),
    NORTH_AMERICA(R.string.north_area, R.drawable.north_america),
    SOUTH_AMERICA(R.string.south_area, R.drawable.south_america),
    CARIBBEAN(R.string.caribbean, R.drawable.blank_space),
    CENTRAL_AMERICA(R.string.central_area, R.drawable.central_america),

    ALL_ASIA(R.string.all_area, R.drawable.blank_space),
    WESTERN_ASIA(R.string.western, R.drawable.western_asia),
    SOUTH_EASTERN_ASIA(R.string.south_eastern, R.drawable.south_eastern_asia),
    SOUTHERN_ASIA(R.string.southern, R.drawable.southern_asia),
    CENTRAL_ASIA(R.string.central_area, R.drawable.central_asia),
    EASTERN_ASIA(R.string.eastern, R.drawable.eastern_asia),

    ALL_EUROPE(R.string.all_area, R.drawable.all_europe),
    EASTERN_EUROPE(R.string.eastern, R.drawable.eastern_europe),
    SOUTH_EASTERN_EUROPE(R.string.south_eastern, R.drawable.south_east_europe),
    NORTHERN_EUROPE(R.string.northern, R.drawable.northern_europe),
    SOUTHERN_EUROPE(R.string.southern, R.drawable.blank_space),
    CENTRAL_EUROPE(R.string.central_area, R.drawable.central_europe),
    WESTERN_EUROPE(R.string.western, R.drawable.blank_space),

    ALL_OCEANIA(R.string.all_area, R.drawable.blank_space),
    MICRONESIA(R.string.micronesia, R.drawable.blank_space),
    POLYNESIA(R.string.polynesia, R.drawable.blank_space),
    MELANESIA(R.string.melanesia, R.drawable.blank_space),
    AUS_NEW(R.string.aus_new, R.drawable.blank_space);

    companion object {
        fun String.toSubRegion() = entries.first {
            it.name.equals(this, ignoreCase = true)
        }
    }
}