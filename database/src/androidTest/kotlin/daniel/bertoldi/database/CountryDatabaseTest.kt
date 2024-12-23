package daniel.bertoldi.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.moshi.Moshi
import daniel.bertoldi.database.CountryEntityFactory.makeInternationalDialResponse
import daniel.bertoldi.database.dao.CountriesDao
import daniel.bertoldi.database.entities.CountryEntity
import daniel.bertoldi.database.typeconverters.InternationalDialInfoTypeConverter
import daniel.bertoldi.network.InternationalDialResponse
import daniel.bertoldi.test.utils.randomList
import daniel.bertoldi.test.utils.randomString
import io.mockk.every
import io.mockk.mockk
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
    @Throws(Exception::class)
    fun onInsertCountries_assertAllDataCorrect() {
        val internationalDialResponse = makeInternationalDialResponse(
            root = randomString(),
            suffixes = randomList<String>()
        )
        val country = CountryEntityFactory.make(
            languages = null,
            idd = internationalDialResponse,
        )
        prepareScenario(fromJsonMock = internationalDialResponse)

        countriesDao.insertCountries(listOf(country))
        val fetchCountry = countriesDao.getAllCountries().first()
        Assert.assertEquals(country.toString(), fetchCountry.toString())
    }

    @Test
    @Throws(Exception::class)
    fun onInsertCountries_assertCorrectNumberOfCountriesInserted() {
        val randomNumberOfCountries = Random.nextInt(from = 2, until = 5)
        val listOfCountries = mutableListOf<CountryEntity>().apply {
            repeat(randomNumberOfCountries) {
                add(CountryEntityFactory.make(countryCode = UUID.randomUUID().toString()))
            }
        }
        countriesDao.insertCountries(listOfCountries)
        val countriesCount = countriesDao.fetchCountriesCount()
        Assert.assertEquals(randomNumberOfCountries, countriesCount)
    }

    @Test
    @Throws(Exception::class)
    fun onInsertCountryWithSameCountryCode_assertSecondCountryIgnored() {
        val firstCountry = CountryEntityFactory.make(countryCode = "COUNTRY_CODE")
        val secondCountry = CountryEntityFactory.make(countryCode = "COUNTRY_CODE")
        countriesDao.insertCountries(listOf(firstCountry, secondCountry))
        val countriesCount = countriesDao.fetchCountriesCount()
        Assert.assertEquals(1, countriesCount)
    }

    @Test
    @Throws(Exception::class)
    fun onFetchCountriesInContinent_assertCorrectListOfCountriesInSpecifiedContinent() {
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
        val countriesInAfrica = countriesDao.fetchCountriesInContinent("Africa")
        Assert.assertEquals(3, countriesInAfrica.size)
    }

    @Test
    @Throws(Exception::class)
    fun onFetchCountriesInContinent_withInvalidContinent_assertNoCountriesReturned() {
        val listOfCountries = mutableListOf<CountryEntity>().apply {
            add(
                CountryEntityFactory.make(
                    continents = listOf("Americas", "Europe", "Africa", "Oceania", "Asia")
                )
            )
        }
        countriesDao.insertCountries(listOfCountries)
        val countries = countriesDao.fetchCountriesInContinent("No Continent")
        Assert.assertEquals(0, countries.size)
    }

    @Test
    @Throws(Exception::class)
    fun onDeleteCountry_assertCorrectNumberOfCountriesInDatabase() {
        val countryToBeDeleted = CountryEntityFactory.make()
        val listOfCountries = listOf(
            CountryEntityFactory.make(),
            countryToBeDeleted,
            CountryEntityFactory.make(),
            CountryEntityFactory.make(),
        )
        countriesDao.insertCountries(listOfCountries)
        countriesDao.deleteCountry(countryToBeDeleted)
        val remainingCountries = countriesDao.getAllCountries()
        Assert.assertEquals(3, remainingCountries.size)
    }

    private fun prepareScenario(
        toJsonMock: String = randomString(),
        fromJsonMock: InternationalDialResponse = makeInternationalDialResponse(),
    ) {
        every {
            moshi.adapter(InternationalDialResponse::class.java).toJson(any())
        } returns toJsonMock
        every {
            moshi.adapter(InternationalDialResponse::class.java).fromJson(toJsonMock)
        } returns fromJsonMock
    }
}