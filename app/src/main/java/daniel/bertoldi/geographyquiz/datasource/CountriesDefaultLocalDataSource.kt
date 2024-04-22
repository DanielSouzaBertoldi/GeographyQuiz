package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.database.DatabaseStuff
import daniel.bertoldi.geographyquiz.domain.mapper.CountryEntityToModelMapper
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import daniel.bertoldi.geographyquiz.domain.repository.CountryModelToEntityMapper
import javax.inject.Inject

class CountriesDefaultLocalDataSource @Inject constructor(
    private val databaseStuff: DatabaseStuff,
    private val entityToModelMapper: CountryEntityToModelMapper,
    private val modelToEntityMapper: CountryModelToEntityMapper,
) : CountriesLocalDataSource {
    // TODO: Change to a flow
    override suspend fun fetchCountriesDb(): List<CountryModel> {
        return entityToModelMapper.mapFrom(databaseStuff.getAllCountries())
    }

    override suspend fun saveCountriesInDb(countries: List<CountryModel>) {
        val countriesEntities = modelToEntityMapper.mapFrom(countries)
        databaseStuff.saveCountries(countriesEntities)
    }
}