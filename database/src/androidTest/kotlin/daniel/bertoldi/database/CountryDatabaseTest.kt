package daniel.bertoldi.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import daniel.bertoldi.database.typeconverters.InternationalDialTypeConverter
import daniel.bertoldi.network.BaseCountryDataResponse
import daniel.bertoldi.network.InternationalDialResponse
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CountryDatabaseTest {
    private val moshi: Moshi = mockk()
    private lateinit var countriesDao: CountriesDao
    private lateinit var database: CountriesDatabase

    //TODO: Honestly wtf is this??? I've no idea what I'm doing!!!
    @Before
    fun createDb() {
        every { moshi.adapter(InternationalDialResponse::class.java).toJson(any()) } returns "yeah"
        every { moshi.adapter(InternationalDialResponse::class.java).fromJson("yeah") } returns InternationalDialResponse("+5", listOf("5"))
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room
            .inMemoryDatabaseBuilder(context, CountriesDatabase::class.java)
            .addTypeConverter(InternationalDialTypeConverter(moshi))
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
    fun writeCountryAndReadInList() {
        val country = CountryEntityFactory.make(languages = null)
        countriesDao.insertCountries(listOf(country))
        val fetchCountry = countriesDao.getAll().first()
        // TODO: apparently I can't assertEquals() two entities, even though they are data class.
        //  Their hashCode is different even though their content is the same.
        Assert.assertEquals(fetchCountry.name, country.name)
    }
}