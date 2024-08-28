package daniel.bertoldi.database.typeconverters

import androidx.room.TypeConverter
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class DurationToLongConverter {

    @TypeConverter
    fun fromDuration(value: Duration) = value.inWholeMilliseconds

    @TypeConverter
    fun toDuration(value: Long) = value.toDuration(DurationUnit.MILLISECONDS)
}