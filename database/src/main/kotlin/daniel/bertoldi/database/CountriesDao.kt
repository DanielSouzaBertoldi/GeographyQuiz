package daniel.bertoldi.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow

@Dao
interface CountriesDao {
    @Query("SELECT * FROM countries")
    fun getAll(): Flow<List<CountryEntity>>

    @Query("SELECT COUNT(*) FROM countries")
    fun fetchCountriesCount(): Flow<Int>

    @Query("SELECT * FROM countries WHERE continents LIKE '%' || :continent || '%'")
    fun fetchCountriesInContinent(continent: String): Flow<List<CountryEntity>>

    @Query("SELECT DISTINCT subRegion FROM countries WHERE region = :region")
    fun fetchAreasInRegion(region: String): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountries(countries: List<CountryEntity>)

    @Delete
    suspend fun deleteCountry(country: CountryEntity)
}