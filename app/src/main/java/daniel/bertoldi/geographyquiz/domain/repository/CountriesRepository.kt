package daniel.bertoldi.geographyquiz.domain.repository

import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import kotlinx.coroutines.flow.Flow

interface CountriesRepository {
    suspend fun getCountries(): List<CountryModel>
    suspend fun getAreasInRegion(region: String): Flow<List<String>>
}