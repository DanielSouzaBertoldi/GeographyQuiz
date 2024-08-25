package daniel.bertoldi.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import daniel.bertoldi.database.typeconverters.CarRegulationsTypeConverter
import daniel.bertoldi.database.typeconverters.CountryNamesTypeConverter
import daniel.bertoldi.database.typeconverters.InternationalDialInfoTypeConverter
import daniel.bertoldi.database.typeconverters.ListStringTypeConverter
import daniel.bertoldi.database.typeconverters.MapStringStringTypeConverter

@Database(entities = [CountryEntity::class], version = 1)
@TypeConverters(
    ListStringTypeConverter::class,
    MapStringStringTypeConverter::class,
    InternationalDialInfoTypeConverter::class,
    CountryNamesTypeConverter::class,
    CarRegulationsTypeConverter::class,
)
abstract class CountriesDatabase : RoomDatabase() {
    abstract fun countriesDao(): CountriesDao
}
