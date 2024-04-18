package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.database.CountryEntity
import daniel.bertoldi.database.DatabaseStuff
import javax.inject.Inject

class CountriesDefaultLocalDataSource @Inject constructor(
    private val databaseStuff: DatabaseStuff,
) : CountriesLocalDataSource {
    // TODO: Change to a flow
    override suspend fun fetchCountriesDb() = databaseStuff.getAllCountries()
}