package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.database.CountryEntity
import daniel.bertoldi.database.DatabaseStuff
import daniel.bertoldi.geographyquiz.domain.mapper.CountryEntityFactory
import daniel.bertoldi.geographyquiz.domain.mapper.CountryEntityToModelMapper
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import daniel.bertoldi.geographyquiz.domain.mapper.CountryModelToEntityMapper
import daniel.bertoldi.geographyquiz.factory.CountryModelFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CountriesDefaultLocalDataSourceTest {

    private val databaseStuff: DatabaseStuff = mockk()
    private val entityToModelMapper: CountryEntityToModelMapper = mockk()
    private val modelToEntityMapper: CountryModelToEntityMapper = mockk()
    private val dataSource = CountriesDefaultLocalDataSource(
        databaseStuff = databaseStuff,
        entityToModelMapper = entityToModelMapper,
        modelToEntityMapper = modelToEntityMapper,
    )

    @Test
    fun fetchCountries_assertCorrectCountriesFetched() = runTest {
        val countries = listOf(
            CountryModelFactory.make(),
            CountryModelFactory.make(),
            CountryModelFactory.make(),
        )
        prepareScenario(
            databaseCountries = listOf(CountryEntityFactory.make()),
            entityToModelResult = countries,
        )

        val actual = dataSource.fetchCountriesDb()

        Assertions.assertEquals(countries, actual)
    }

    @Test
    fun fetchCountries_withNoCountriesInDb_assertEmptyListReturned() = runTest {
        prepareScenario(
            databaseCountries = emptyList(),
            entityToModelResult = emptyList(),
        )

        val actual = dataSource.fetchCountriesDb()

        Assertions.assertEquals(emptyList<CountryModel>(), actual)
    }

    @Test
    fun saveCountriesInDb_withCountriesList_verifyCorrectCountriesSaved() = runTest {
        val countriesEntities = listOf(
            CountryEntityFactory.make(),
            CountryEntityFactory.make(),
            CountryEntityFactory.make(),
        )

        prepareScenario(
            modelToEntitiesResult = countriesEntities,
        )

        dataSource.saveCountriesInDb(listOf(CountryModelFactory.make()))

        coVerify(exactly = 1) {
            databaseStuff.saveCountries(countriesEntities)
        }
    }

    @Test
    fun saveCountriesInDb_withEmptyCountriesList_verifyEmptyCountriesSaved() = runTest {
        prepareScenario(
            modelToEntitiesResult = emptyList(),
        )

        dataSource.saveCountriesInDb(listOf(CountryModelFactory.make()))

        coVerify(exactly = 1) {
            databaseStuff.saveCountries(emptyList())
        }
    }

    private fun prepareScenario(
        databaseCountries: List<CountryEntity> = listOf(CountryEntityFactory.make()),
        entityToModelResult: List<CountryModel> = listOf(CountryModelFactory.make()),
        modelToEntitiesResult: List<CountryEntity> = listOf(CountryEntityFactory.make())
    ) {
        // TODO: is this ok?
        val fakeFlow = flow {
            emit(databaseCountries)
        }

        coEvery { databaseStuff.getAllCountries() } returns fakeFlow
        coEvery { entityToModelMapper.mapFrom(any()) } returns entityToModelResult
        coEvery { modelToEntityMapper.mapFrom(any()) } returns modelToEntitiesResult

        coEvery { databaseStuff.saveCountries(any()) } just runs
    }
}