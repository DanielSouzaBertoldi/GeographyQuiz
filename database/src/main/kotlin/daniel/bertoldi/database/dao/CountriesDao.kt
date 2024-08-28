package daniel.bertoldi.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import daniel.bertoldi.database.entities.CountryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountriesDao {
    @Query("SELECT * FROM countries")
    fun getAllCountries(): Flow<List<CountryEntity>>

    @Query("SELECT COUNT(*) FROM countries")
    fun fetchCountriesCount(): Flow<Int>

    @Query("SELECT * FROM countries WHERE continents LIKE '%' || :continent || '%'")
    fun fetchCountriesInContinent(continent: String): Flow<List<CountryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountries(countries: List<CountryEntity>)

    @Delete
    suspend fun deleteCountry(country: CountryEntity)

    @Query("SELECT * FROM countries WHERE region = :region")
    fun fetchCountriesFromRegion(region: String): Flow<List<CountryEntity>>

    @Query("SELECT * FROM countries WHERE region = :region AND subRegion LIKE '%' || :subRegion || '%'")
    fun fetchCountriesGivenRegionAndSubRegion(region: String, subRegion: String): Flow<List<CountryEntity>>
}