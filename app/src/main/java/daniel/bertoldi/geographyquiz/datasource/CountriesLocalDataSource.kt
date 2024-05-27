package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import kotlinx.coroutines.flow.Flow

interface CountriesLocalDataSource {

    suspend fun fetchCountriesDb(): Flow<List<CountryModel>>
    suspend fun saveCountriesInDb(countries: List<CountryModel>)
    suspend fun fetchCountriesGivenArea(region: String, subRegion: String): Flow<List<CountryModel>>
}