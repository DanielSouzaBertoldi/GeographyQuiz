package daniel.bertoldi.database.typeconverters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import daniel.bertoldi.network.InternationalDialResponse

@ProvidedTypeConverter
class InternationalDialTypeConverter(private val moshi: Moshi) {

    /* faz sentido eu ter esses type converters aqui?
    * ou seria melhor estar dentro da feature que usa? hmm */
    @TypeConverter
    fun fromInternationalDialModel(value: InternationalDialResponse): String {
        return moshi.adapter(InternationalDialResponse::class.java).toJson(value)
    }

    @TypeConverter
    fun toInternationalDialModel(value: String): InternationalDialResponse? {
        return moshi.adapter(InternationalDialResponse::class.java).fromJson(value)
    }
}