package daniel.bertoldi.database

import daniel.bertoldi.network.BaseCountryDataResponse
import kotlinx.coroutines.flow.Flow

interface DatabaseInterface {

    suspend fun getAllCountries(): Flow<List<CountryEntity>>
    suspend fun fetchCountriesCount(): Flow<Int>
    suspend fun saveCountries(countries: List<CountryEntity>)
    suspend fun fetchCountriesInContinent(continent: String): Flow<List<CountryEntity>>
}