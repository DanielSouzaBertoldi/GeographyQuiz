package daniel.bertoldi.database.typeconverters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import daniel.bertoldi.database.entities.InternationalDialInfo

@ProvidedTypeConverter
class InternationalDialInfoTypeConverter(private val moshi: Moshi) {

    @TypeConverter
    fun fromInternationalDialModel(value: InternationalDialInfo): String {
        return moshi.adapter(InternationalDialInfo::class.java).toJson(value)
    }

    @TypeConverter
    fun toInternationalDialModel(value: String): InternationalDialInfo? {
        return moshi.adapter(InternationalDialInfo::class.java).fromJson(value)
    }
}