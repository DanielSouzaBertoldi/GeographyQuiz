package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.database.CountriesDatabaseInterface
import daniel.bertoldi.geographyquiz.domain.mapper.CountryEntityToModelMapper
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import daniel.bertoldi.geographyquiz.domain.mapper.CountryModelToEntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CountriesDefaultLocalDataSource @Inject constructor(
    private val countriesDatabase: CountriesDatabaseInterface,
    private val entityToModelMapper: CountryEntityToModelMapper,
    private val modelToEntityMapper: CountryModelToEntityMapper,
) : CountriesLocalDataSource {

    // TODO: Change to a flow
    override suspend fun fetchCountriesDb(): List<CountryModel> {
        return entityToModelMapper.mapFrom(countriesDatabase.getAllCountries().first())
    }

    override suspend fun saveCountriesInDb(countries: List<CountryModel>) {
        val countriesEntities = modelToEntityMapper.mapFrom(countries)
        countriesDatabase.saveCountries(countriesEntities)
    }
}