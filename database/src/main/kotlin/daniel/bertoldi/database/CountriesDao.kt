package daniel.bertoldi.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters

@Dao
interface CountriesDao {
    @Query("SELECT * FROM countries")
    fun getAll(): List<CountryEntity>

    @Insert
    fun insertCountry(country: CountryEntity)

    @Insert
    fun insertCountries(countries: List<CountryEntity>)

    @Delete
    fun deleteCountry(country: CountryEntity)
}