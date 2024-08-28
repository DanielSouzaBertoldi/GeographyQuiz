package daniel.bertoldi.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import daniel.bertoldi.database.dao.CountriesDao
import daniel.bertoldi.database.dao.HighScoresDao
import daniel.bertoldi.database.entities.CountryEntity
import daniel.bertoldi.database.entities.HighScoresEntity
import daniel.bertoldi.database.typeconverters.CarRegulationsTypeConverter
import daniel.bertoldi.database.typeconverters.CountryNamesTypeConverter
import daniel.bertoldi.database.typeconverters.DateTypeConverter
import daniel.bertoldi.database.typeconverters.DurationTypeConverter
import daniel.bertoldi.database.typeconverters.InternationalDialInfoTypeConverter
import daniel.bertoldi.database.typeconverters.ListStringTypeConverter
import daniel.bertoldi.database.typeconverters.MapStringStringTypeConverter

@Database(
    entities = [
        CountryEntity::class,
        HighScoresEntity::class,
    ],
    version = 1,
)
@TypeConverters(
    ListStringTypeConverter::class,
    MapStringStringTypeConverter::class,
    InternationalDialInfoTypeConverter::class,
    CountryNamesTypeConverter::class,
    CarRegulationsTypeConverter::class,
    DurationTypeConverter::class,
    DateTypeConverter::class,
)
abstract class GeographyQuizDatabase : RoomDatabase() {
    abstract fun countriesDao(): CountriesDao
    abstract fun highScoredDao(): HighScoresDao
}
