package daniel.bertoldi.database.typeconverters

import androidx.room.TypeConverter
import daniel.bertoldi.database.CountryNames

private const val DELIMITER = "|"

class CountryNamesTypeConverter {

    @TypeConverter
    fun fromCountryNamesModel(value: CountryNames): String {
        return "${value.common}$DELIMITER${value.official}"
    }

    @TypeConverter
    fun toCountryNamesModel(value: String): CountryNames {
        val splitString = value.split(DELIMITER)
        return CountryNames(
            common = splitString.component1(),
            official = splitString.component2(),
        )
    }
}