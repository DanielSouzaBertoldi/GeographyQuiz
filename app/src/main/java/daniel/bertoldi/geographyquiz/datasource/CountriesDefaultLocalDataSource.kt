package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.database.CountryEntity
import daniel.bertoldi.database.DatabaseStuff
import daniel.bertoldi.geographyquiz.domain.mapper.CountryEntityToModelMapper
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import javax.inject.Inject

class CountriesDefaultLocalDataSource @Inject constructor(
    private val databaseStuff: DatabaseStuff,
    private val entityToModelMapper: CountryEntityToModelMapper,
) : CountriesLocalDataSource {
    // TODO: Change to a flow
    override suspend fun fetchCountriesDb(): List<CountryModel> {
        return entityToModelMapper.mapFrom(databaseStuff.getAllCountries())
    }
}