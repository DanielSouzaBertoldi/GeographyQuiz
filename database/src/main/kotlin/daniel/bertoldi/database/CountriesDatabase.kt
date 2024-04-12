package daniel.bertoldi.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import daniel.bertoldi.database.typeconverters.InternationalDialTypeConverter
import daniel.bertoldi.database.typeconverters.ListStringTypeConverter
import daniel.bertoldi.database.typeconverters.MapStringStringTypeConverter
import daniel.bertoldi.database.typeconverters.NameDataResponseTypeConverter

@Database(entities = [CountryEntity::class], version = 1)
@TypeConverters(
    ListStringTypeConverter::class,
    MapStringStringTypeConverter::class,
    InternationalDialTypeConverter::class,
    NameDataResponseTypeConverter::class,
)
abstract class CountriesDatabase : RoomDatabase() {
    abstract fun countriesDao(): CountriesDao
}