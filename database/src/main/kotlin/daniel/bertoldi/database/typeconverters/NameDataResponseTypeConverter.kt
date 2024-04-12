package daniel.bertoldi.database.typeconverters

import androidx.room.TypeConverter
import daniel.bertoldi.network.NameDataResponse

private const val DELIMITER = "|"

class NameDataResponseTypeConverter {

    @TypeConverter
    fun fromNameDataResponseModel(value: NameDataResponse): String {
        return "${value.common}$DELIMITER${value.official}"
    }

    @TypeConverter
    fun toNameDataResponseModel(value: String): NameDataResponse {
        val splitString = value.split(DELIMITER)
        return NameDataResponse(
            common = splitString.component1(),
            official = splitString.component2(),
        )
    }
}