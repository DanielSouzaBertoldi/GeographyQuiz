package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import daniel.bertoldi.geographyquiz.presentation.model.Region
import daniel.bertoldi.geographyquiz.presentation.model.SubRegion
import kotlinx.coroutines.flow.Flow

interface CountriesLocalDataSource {

    suspend fun fetchCountriesDb(): Flow<List<CountryModel>>
    suspend fun saveCountriesInDb(countries: List<CountryModel>)
    suspend fun fetchCountriesGivenArea(region: String, subRegion: String): Flow<List<CountryModel>>
}