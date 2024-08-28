package daniel.bertoldi.database.typeconverters

import androidx.room.TypeConverter
import daniel.bertoldi.database.entities.CarRegulations

private const val DELIMITER = "&"
private const val STRING_LIST_DELIMITER = ","

class CarRegulationsTypeConverter {

    @TypeConverter
    fun fromCarRegulations(value: CarRegulations) =
        "${value.carSigns?.coolToString()}$DELIMITER${value.carSide}"

    @TypeConverter
    fun toCarRegulations(value: String): CarRegulations {
        val splitString = value.split(DELIMITER)
        return CarRegulations(
            carSigns = splitString.component1().split(STRING_LIST_DELIMITER),
            carSide = splitString.component2(),
        )
    }

    private fun List<String>.coolToString() = this.joinToString(STRING_LIST_DELIMITER)
}