package daniel.bertoldi.geographyquiz.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

// TODO: I can just use SubRegion enum class directly.
data class AreaUiModel(
    @StringRes val name: Int,
    @DrawableRes val icon: Int,
)
