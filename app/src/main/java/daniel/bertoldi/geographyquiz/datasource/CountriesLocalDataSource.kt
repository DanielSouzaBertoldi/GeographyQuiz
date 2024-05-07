package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import kotlinx.coroutines.flow.Flow

interface CountriesLocalDataSource {

    suspend fun fetchCountriesDb(): List<CountryModel>

    suspend fun saveCountriesInDb(countries: List<CountryModel>)

    suspend fun fetchAreasInRegion(region: String): Flow<List<String>>
}