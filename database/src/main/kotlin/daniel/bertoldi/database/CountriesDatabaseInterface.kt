package daniel.bertoldi.database

import kotlinx.coroutines.flow.Flow

interface CountriesDatabaseInterface {

    suspend fun getAllCountries(): Flow<List<CountryEntity>>
    suspend fun fetchCountriesCount(): Flow<Int>
    suspend fun saveCountries(countries: List<CountryEntity>)
    suspend fun fetchCountriesInContinent(continent: String): Flow<List<CountryEntity>>
    suspend fun fetchCountriesFromRegion(region: String): Flow<List<CountryEntity>>
    suspend fun fetchCountriesFromArea(region: String, subRegion: String): Flow<List<CountryEntity>>
}