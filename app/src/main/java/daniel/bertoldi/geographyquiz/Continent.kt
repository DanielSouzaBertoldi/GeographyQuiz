package daniel.bertoldi.geographyquiz

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class Continent(
    val simpleName: String,
    @StringRes val countryString: Int,
    @DrawableRes val countryIcon: Int,
) {
    AFRICA("Africa", R.string.africa, R.drawable.africa),
    AMERICAS("Americas", R.string.americas, R.drawable.americas),
    ASIA("Asia", R.string.asia, R.drawable.asia),
    EUROPE("Europe", R.string.europe, R.drawable.europe),
    OCEANIA("Oceania", R.string.oceania, R.drawable.australia)
}