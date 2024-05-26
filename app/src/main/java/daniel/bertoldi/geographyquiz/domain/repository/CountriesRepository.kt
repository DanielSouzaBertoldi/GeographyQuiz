package daniel.bertoldi.geographyquiz.domain.repository

import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import kotlinx.coroutines.flow.Flow

interface CountriesRepository {
    suspend fun fetchCountries(): Flow<List<CountryModel>>
    suspend fun getCountries(region: String, subRegion: String): Flow<List<CountryModel>>
}