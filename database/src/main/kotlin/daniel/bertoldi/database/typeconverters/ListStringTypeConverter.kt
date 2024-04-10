package daniel.bertoldi.database.typeconverters

import androidx.room.TypeConverter

class ListStringTypeConverter {

    @TypeConverter
    fun fromListOfStrings(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toListOfStrings(value: String?): List<String>? {
        return value?.split(",")
    }
}