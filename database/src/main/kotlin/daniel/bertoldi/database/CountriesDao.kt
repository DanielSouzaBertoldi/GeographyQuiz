package daniel.bertoldi.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters

@Dao
interface CountriesDao {
    @Query("SELECT * FROM countries")
    fun getAll(): List<CountryEntity>

    @Query("SELECT COUNT(*) FROM countries")
    fun fetchCountriesCount(): Int

    @Query("SELECT * FROM countries WHERE continents LIKE :continent")
    fun fetchCountriesInContinent(continent: String): List<CountryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCountries(countries: List<CountryEntity>)

    @Delete
    fun deleteCountry(country: CountryEntity)
}