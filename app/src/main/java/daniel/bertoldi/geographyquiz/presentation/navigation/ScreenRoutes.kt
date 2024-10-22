package daniel.bertoldi.geographyquiz.presentation.navigation

import kotlinx.serialization.Serializable

interface ScreenRoutes {
    @Serializable
    data object Home : ScreenRoutes

    @Serializable
    data object RegionSelection : ScreenRoutes

    @Serializable
    data class AreaSelection(val region: String) : ScreenRoutes

    @Serializable
    data class GameModeSelection(
        val region: String,
        val area: String,
    ) : ScreenRoutes

    @Serializable
    data class FlagGame(
        val region: String,
        val subRegion: String,
        val gameMode: String,
    ) : ScreenRoutes
}