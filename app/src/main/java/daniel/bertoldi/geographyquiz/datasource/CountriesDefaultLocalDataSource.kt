package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.database.CountriesDatabaseInterface
import daniel.bertoldi.geographyquiz.domain.mapper.CountryEntityToModelMapper
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import daniel.bertoldi.geographyquiz.domain.mapper.CountryModelToEntityMapper
import daniel.bertoldi.geographyquiz.presentation.model.Region
import daniel.bertoldi.geographyquiz.presentation.model.SubRegion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

private const val ALL_AREA = "All"

class CountriesDefaultLocalDataSource @Inject constructor(
    private val countriesDatabase: CountriesDatabaseInterface,
    private val entityToModelMapper: CountryEntityToModelMapper,
    private val modelToEntityMapper: CountryModelToEntityMapper,
) : CountriesLocalDataSource {

    override suspend fun fetchCountriesDb(): Flow<List<CountryModel>> {
        return countriesDatabase.getAllCountries().transform {
            emit(entityToModelMapper.mapFrom(it))
        }
    }

    override suspend fun saveCountriesInDb(countries: List<CountryModel>) {
        val countriesEntities = modelToEntityMapper.mapFrom(countries)
        countriesDatabase.saveCountries(countriesEntities)
    }

    override suspend fun fetchCountriesGivenArea(
        region: String,
        subRegion: String,
    ): Flow<List<CountryModel>> {
        val countries = if (subRegion == ALL_AREA) {
            countriesDatabase.fetchCountriesFromRegion(region)
        } else {
            countriesDatabase.fetchCountriesFromArea(region, subRegion)
        }

        return countries.transform {
            emit(entityToModelMapper.mapFrom(it))
        }
    }
}