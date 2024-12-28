package daniel.bertoldi.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.moshi.Moshi
import daniel.bertoldi.database.dao.CountriesDao
import daniel.bertoldi.database.entities.CountryEntity
import daniel.bertoldi.database.entities.InternationalDialInfo
import daniel.bertoldi.database.typeconverters.InternationalDialInfoTypeConverter
import daniel.bertoldi.utilities.test.utils.randomList
import daniel.bertoldi.utilities.test.utils.randomString
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.UUID
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class CountryDatabaseTest {
    private val moshi: Moshi = mockk()
    private lateinit var countriesDao: CountriesDao
    private lateinit var database: GeographyQuizDatabase

    @Before
    fun createDb() {
        prepareScenario()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room
            .inMemoryDatabaseBuilder(context, GeographyQuizDatabase::class.java)
            .addTypeConverter(InternationalDialInfoTypeConverter(moshi))
            .build()
        countriesDao = database.countriesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun onInsertCountries_assertAllDataCorrect() = runTest {
        val internationalDialInfo = CountryEntityFactory.makeInternationalDialInfo(
            root = randomString(),
            suffixes = randomList<String>()
        )
        val country = CountryEntityFactory.make(
            languages = null,
            idd = internationalDialInfo,
        )
        prepareScenario(fromJsonMock = internationalDialInfo)

        countriesDao.insertCountries(listOf(country))
        val fetchCountry = countriesDao.getAllCountries().first()
        Assert.assertEquals(country.toString(), fetchCountry.first().toString())
    }

    @Test
    @Throws(Exception::class)
    fun onInsertCountries_assertCorrectNumberOfCountriesInserted() = runTest {
        val randomNumberOfCountries = Random.nextInt(from = 2, until = 5)
        val listOfCountries = mutableListOf<CountryEntity>().apply {
            repeat(randomNumberOfCountries) {
                add(CountryEntityFactory.make(countryCode = UUID.randomUUID().toString()))
            }
        }
        countriesDao.insertCountries(listOfCountries)
        val countriesCount = countriesDao.fetchCountriesCount().first()
        Assert.assertEquals(randomNumberOfCountries.toString(), countriesCount.toString())
    }

    @Test
    @Throws(Exception::class)
    fun onInsertCountryWithSameCountryCode_assertSecondCountryIgnored() = runTest {
        val firstCountry = CountryEntityFactory.make(countryCode = "COUNTRY_CODE")
        val secondCountry = CountryEntityFactory.make(countryCode = "COUNTRY_CODE")
        countriesDao.insertCountries(listOf(firstCountry, secondCountry))
        val countriesCount = countriesDao.fetchCountriesCount().first()
        Assert.assertEquals(1, countriesCount)
    }

    @Test
    @Throws(Exception::class)
    fun onFetchCountriesInContinent_assertCorrectListOfCountriesInSpecifiedContinent() = runTest {
        val listOfCountries = mutableListOf<CountryEntity>().apply {
            add(CountryEntityFactory.make(continents = listOf("Americas")))
            add(CountryEntityFactory.make(continents = listOf("Africa")))
            add(CountryEntityFactory.make(continents = listOf("Africa")))
            add(CountryEntityFactory.make(continents = listOf("Europe", "Americas")))
            add(CountryEntityFactory.make(continents = listOf("Asia")))
            add(CountryEntityFactory.make(continents = listOf("Oceania")))
            add(CountryEntityFactory.make(continents = listOf("Africa", "Europe")))
        }
        countriesDao.insertCountries(listOfCountries)
        val countriesInAfrica = countriesDao.fetchCountriesInContinent("Africa").first()
        Assert.assertEquals(3, countriesInAfrica.size)
    }

    @Test
    @Throws(Exception::class)
    fun onFetchCountriesInContinent_withInvalidContinent_assertNoCountriesReturned() = runTest {
        val listOfCountries = mutableListOf<CountryEntity>().apply {
            add(
                CountryEntityFactory.make(
                    continents = listOf("Americas", "Europe", "Africa", "Oceania", "Asia")
                )
            )
        }
        countriesDao.insertCountries(listOfCountries)
        val countries = countriesDao.fetchCountriesInContinent("No Continent").first()
        Assert.assertEquals(0, countries.size)
    }

    @Test
    @Throws(Exception::class)
    fun onDeleteCountry_assertCorrectNumberOfCountriesInDatabase() = runTest {
        val countryToBeDeleted = CountryEntityFactory.make()
        val listOfCountries = listOf(
            CountryEntityFactory.make(),
            countryToBeDeleted,
            CountryEntityFactory.make(),
            CountryEntityFactory.make(),
        )
        countriesDao.insertCountries(listOfCountries)
        countriesDao.deleteCountry(countryToBeDeleted)
        val remainingCountries = countriesDao.getAllCountries().first()
        Assert.assertEquals(3, remainingCountries.size)
    }

    private fun prepareScenario(
        toJsonMock: String = randomString(),
        fromJsonMock: InternationalDialInfo = CountryEntityFactory.makeInternationalDialInfo(),
    ) {
        every {
            moshi.adapter(InternationalDialInfo::class.java).toJson(any())
        } returns toJsonMock
        every {
            moshi.adapter(InternationalDialInfo::class.java).fromJson(toJsonMock)
        } returns fromJsonMock
    }
}