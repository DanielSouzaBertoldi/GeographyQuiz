package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.database.CountriesDao
import daniel.bertoldi.geographyquiz.domain.mapper.CountryEntityToModelMapper
import daniel.bertoldi.geographyquiz.domain.mapper.CountryModelToEntityMapper
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

private const val ALL_AREA = "All"

class CountriesDefaultLocalDataSource @Inject constructor(
    private val countriesDao: CountriesDao,
    private val entityToModelMapper: CountryEntityToModelMapper,
    private val modelToEntityMapper: CountryModelToEntityMapper,
) : CountriesLocalDataSource {

    override suspend fun fetchCountriesDb(): Flow<List<CountryModel>> {
        return countriesDao.getAllCountries().transform {
            emit(entityToModelMapper.mapFrom(it))
        }
    }

    override suspend fun saveCountriesInDb(countries: List<CountryModel>) {
        val countriesEntities = modelToEntityMapper.mapFrom(countries)
        countriesDao.insertCountries(countriesEntities)
    }

    override suspend fun fetchCountriesInSubRegion(
        region: String,
        subRegion: String,
    ): Flow<List<CountryModel>> {
        val countries = if (subRegion == ALL_AREA) {
            countriesDao.fetchCountriesFromRegion(region)
        } else {
            countriesDao.fetchCountriesGivenRegionAndSubRegion(region, subRegion)
        }

        return countries.transform {
            emit(entityToModelMapper.mapFrom(it))
        }
    }
}