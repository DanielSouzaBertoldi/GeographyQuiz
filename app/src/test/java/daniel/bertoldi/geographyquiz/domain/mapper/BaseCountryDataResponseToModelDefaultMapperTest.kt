package daniel.bertoldi.geographyquiz.domain.mapper

import daniel.bertoldi.network.BaseCountryDataResponse
import daniel.bertoldi.network.BaseCountryDataResponseFactory
import daniel.bertoldi.utilities.test.utils.randomInt
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BaseCountryDataResponseToModelDefaultMapperTest {

    private val mapper = BaseCountryDataResponseToModelDefaultMapper()

    @Test
    fun mapFrom_withNullResponse_assertActualNull() {
        val actual = mapper.mapFrom(null)

        Assertions.assertNull(actual)
    }

    @Test
    fun mapFrom_withEmptyResponse_assertActualEmptyList() {
        val actual = mapper.mapFrom(emptyList())

        Assertions.assertEquals(emptyList<BaseCountryDataResponse>(), actual)
    }

    @Test
    fun mapFrom_withRandomNumberOfCountries_assertActualSizeTheSame() {
        val countries = mutableListOf<BaseCountryDataResponse>().apply {
            repeat(randomInt()) {
                add(BaseCountryDataResponseFactory.make())
            }
        }

        val actual = mapper.mapFrom(countries)

        Assertions.assertEquals(countries.size, actual!!.size)
    }

    @Test
    fun mapFrom_withValidResponse_assertBasePropertiesEquals() {
        val countryResponse = BaseCountryDataResponseFactory.make()
        val response = listOf(countryResponse)

        val actual = mapper.mapFrom(response)!!.first()

        Assertions.assertEquals(countryResponse.countryCode, actual.countryCode)
        Assertions.assertEquals(countryResponse.topLevelDomains, actual.topLevelDomains)
        Assertions.assertEquals(countryResponse.independent, actual.independent)
        Assertions.assertEquals(countryResponse.unMember, actual.unMember)
        Assertions.assertEquals(countryResponse.capital, actual.capital)
        Assertions.assertEquals(countryResponse.alternativeSpellings, actual.altSpellings)
        Assertions.assertEquals(countryResponse.region, actual.region)
        Assertions.assertEquals(countryResponse.subRegion, actual.subRegion)
        Assertions.assertEquals(countryResponse.languages, actual.languages)
        Assertions.assertEquals(countryResponse.area, actual.area)
        Assertions.assertEquals(countryResponse.emojiFlag, actual.emojiFlag)
        Assertions.assertEquals(countryResponse.population, actual.population)
        Assertions.assertEquals(countryResponse.timezones, actual.timezones)
        Assertions.assertEquals(countryResponse.continents, actual.continents)
        Assertions.assertEquals(countryResponse.flags.png, actual.flagPng)
        Assertions.assertEquals(countryResponse.coatOfArms.png, actual.coatOfArms)
        Assertions.assertEquals(countryResponse.startOfWeek, actual.startOfWeek.name)
    }

    @Test
    fun mapFrom_withValidResponse_assertCountryNameModelCorrectlyMapped() {
        val countryNameResponse = BaseCountryDataResponseFactory.makeCountryName()
        val countryResponse = BaseCountryDataResponseFactory.make(
            name = countryNameResponse,
        )
        val response = listOf(countryResponse)

        val actual = mapper.mapFrom(response)!!.first()

        Assertions.assertEquals(countryNameResponse.common, actual.name.common)
        Assertions.assertEquals(countryNameResponse.official, actual.name.official)
    }

    @Test
    fun mapFrom_withValidResponse_assertCountryCallingCodesCorrectlyMapped() {
        val countryCallingCodesResponse =
            BaseCountryDataResponseFactory.makeInternationalDialResponse()
        val countryResponse = BaseCountryDataResponseFactory.make(
            idd = countryCallingCodesResponse,
        )
        val response = listOf(countryResponse)

        val actual = mapper.mapFrom(response)!!.first()

        Assertions.assertEquals(
            countryCallingCodesResponse.root,
            actual.internationalDialResponse.root,
        )
        Assertions.assertEquals(
            countryCallingCodesResponse.suffixes,
            actual.internationalDialResponse.suffixes,
        )
    }

    @Test
    fun mapFrom_withValidResponse_assertCountryCarInfoCorrectlyMapped() {
        val countryCarResponse = BaseCountryDataResponseFactory.makeCarResponse()
        val countryResponse = BaseCountryDataResponseFactory.make(
            car = countryCarResponse,
        )
        val response = listOf(countryResponse)

        val actual = mapper.mapFrom(response)!!.first()

        Assertions.assertEquals(countryCarResponse.signs, actual.carInfo.signs)
        Assertions.assertEquals(countryCarResponse.side, actual.carInfo.side)
    }
}