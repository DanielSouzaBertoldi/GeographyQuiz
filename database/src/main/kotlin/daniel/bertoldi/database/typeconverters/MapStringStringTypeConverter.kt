package daniel.bertoldi.database.typeconverters

import androidx.room.TypeConverter
import java.util.TreeMap

class MapStringStringTypeConverter {

    @TypeConverter
    fun fromStringMap(value: Map<String, String>?): String? {
        return value?.let {
            val sortedMap = TreeMap(value)
            sortedMap.keys.joinToString(",").plus("<div>").plus(sortedMap.values.joinToString(","))
        }
    }

    @TypeConverter
    fun toStringMap(value: String?): Map<String, String>? {
        return value?.split("<div>")?.run {
            val keys = getOrNull(0)?.split(",")?.map { it }
            val values = getOrNull(1)?.split(",")?.map { it }

            val res = hashMapOf<String, String>()
            keys?.forEachIndexed { index, s ->
                res[s] = values?.getOrNull(index) ?: ""
            }
            res
        }
    }
}