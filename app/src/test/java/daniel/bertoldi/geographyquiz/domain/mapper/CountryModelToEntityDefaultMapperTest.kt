package daniel.bertoldi.geographyquiz.domain.mapper

import daniel.bertoldi.geographyquiz.factory.CountryModelFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CountryModelToEntityDefaultMapperTest {

    private val mapper = CountryModelToEntityDefaultMapper()

    @Test
    fun withBaseModelParams_asserCorrectEntityMapping() {
        val country = CountryModelFactory.make()

        val actual = mapper.mapFrom(listOf(country)).first()

        Assertions.assertEquals(country.countryCode, actual.countryCode)
        Assertions.assertEquals(country.topLevelDomains, actual.tld)
        Assertions.assertEquals(country.independent, actual.independent)
        Assertions.assertEquals(country.unMember, actual.unMember)
        Assertions.assertEquals(country.capital, actual.capital)
        Assertions.assertEquals(country.altSpellings, actual.altSpellings)
        Assertions.assertEquals(country.region, actual.region)
        Assertions.assertEquals(country.subRegion, actual.subRegion)
        Assertions.assertEquals(country.languages, actual.languages)
        Assertions.assertEquals(country.landlocked, actual.landlocked)
        Assertions.assertEquals(country.area, actual.area)
        Assertions.assertEquals(country.emojiFlag, actual.emojiFlag)
        Assertions.assertEquals(country.population, actual.population)
        Assertions.assertEquals(country.carInfo.side, actual.carSide)
        Assertions.assertEquals(country.carInfo.signs, actual.carSigns)
        Assertions.assertEquals(country.timezones, actual.timezones)
        Assertions.assertEquals(country.continents, actual.continents)
        Assertions.assertEquals(country.flagPng, actual.flagPng)
        Assertions.assertEquals(country.coatOfArms, actual.coatOfArms)
        Assertions.assertEquals(country.startOfWeek.name, actual.startOfWeek)
    }

    @Test
    fun withCountryNameParams_asserCorrectNameDataEntityMapping() {
        val country = CountryModelFactory.make()

        val actual = mapper.mapFrom(listOf(country)).first()

        Assertions.assertEquals(country.name.common, actual.name.common)
        Assertions.assertEquals(country.name.official, actual.name.official)
    }

    @Test
    fun withCountryIddParams_asserCorrectIddDataEntityMapping() {
        val country = CountryModelFactory.make()

        val actual = mapper.mapFrom(listOf(country)).first()

        Assertions.assertEquals(country.internationalDialResponse.root, actual.idd.root)
        Assertions.assertEquals(country.internationalDialResponse.suffixes, actual.idd.suffixes)
    }
}