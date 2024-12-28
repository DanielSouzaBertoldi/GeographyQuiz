package daniel.bertoldi.geographyquiz.domain.mapper

import daniel.bertoldi.database.CountryEntityFactory
import daniel.bertoldi.database.entities.CarRegulations
import daniel.bertoldi.database.entities.CountryEntity
import daniel.bertoldi.utilities.test.utils.randomInt
import daniel.bertoldi.utilities.test.utils.randomList
import daniel.bertoldi.utilities.test.utils.randomString
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CountryEntityToModelDefaultMapperTest {

    private val mapper = CountryEntityToModelDefaultMapper()

    @Test
    fun mapFrom_withEmptyList_assertActualEmptyList() {
        val actual = mapper.mapFrom(emptyList())

        Assertions.assertEquals(emptyList<CountryEntity>(), actual)
    }

    @Test
    fun mapFrom_withRandomNumberOfCountryEntities_assertActualHasSameSize() {
        val countryEntities = mutableListOf<CountryEntity>().apply {
            repeat(randomInt()) {
                add(CountryEntityFactory.make())
            }
        }

        val actual = mapper.mapFrom(countryEntities)

        Assertions.assertEquals(countryEntities.size, actual.size)
    }

    @Test
    fun mapFrom_withCountryEntity_assertBasePropertiesCorrectlyMapped() {
        val countryEntity = CountryEntityFactory.make()

        val actual = mapper.mapFrom(listOf(countryEntity)).first()

        Assertions.assertEquals(countryEntity.countryCode, actual.countryCode)
        Assertions.assertEquals(countryEntity.tld, actual.topLevelDomains)
        Assertions.assertEquals(countryEntity.independent, actual.independent)
        Assertions.assertEquals(countryEntity.unMember, actual.unMember)
        Assertions.assertEquals(countryEntity.capital, actual.capital)
        Assertions.assertEquals(countryEntity.altSpellings, actual.altSpellings)
        Assertions.assertEquals(countryEntity.region, actual.region)
        Assertions.assertEquals(countryEntity.subRegion, actual.subRegion)
        Assertions.assertEquals(countryEntity.languages, actual.languages)
        Assertions.assertEquals(countryEntity.landlocked, actual.landlocked)
        Assertions.assertEquals(countryEntity.area, actual.area)
        Assertions.assertEquals(countryEntity.emojiFlag, actual.emojiFlag)
        Assertions.assertEquals(countryEntity.population, actual.population)
        Assertions.assertEquals(countryEntity.timezones, actual.timezones)
        Assertions.assertEquals(countryEntity.continents, actual.continents)
        Assertions.assertEquals(countryEntity.flagPng, actual.flagPng)
        Assertions.assertEquals(countryEntity.coatOfArms, actual.coatOfArms)
        Assertions.assertEquals(countryEntity.startOfWeek, actual.startOfWeek.name)
    }

    @Test
    fun mapFrom_withCountryEntity_assertCountryNameCorrectlyMapped() {
        val countryName = CountryEntityFactory.makeCountryNames()
        val countryEntity = CountryEntityFactory.make(name = countryName)
        val actual = mapper.mapFrom(listOf(countryEntity)).first()

        Assertions.assertEquals(countryEntity.name.common, actual.name.common)
        Assertions.assertEquals(countryEntity.name.official, actual.name.official)
    }

    @Test
    fun mapFrom_withCountryEntity_assertCountryCallingCodesCorrectlyMapped() {
        val countryIdd = CountryEntityFactory.makeInternationalDialInfo()
        val countryEntity = CountryEntityFactory.make(idd = countryIdd)
        val actual = mapper.mapFrom(listOf(countryEntity)).first()

        Assertions.assertEquals(countryEntity.idd.root, actual.internationalDialResponse.root)
        Assertions.assertEquals(
            countryEntity.idd.suffixes,
            actual.internationalDialResponse.suffixes,
        )
    }

    @Test
    fun mapFrom_withCountryEntity_assertCountryCarInfoCorrectlyMapped() {
        val carSigns = randomList<String>()
        val carSide = randomString()
        val countryEntity = CountryEntityFactory.make(
            carRegulations = CarRegulations(
                carSigns = carSigns,
                carSide = carSide,
            ),
        )
        val actual = mapper.mapFrom(listOf(countryEntity)).first()

        Assertions.assertEquals(countryEntity.carRegulations.carSide, actual.carInfo.side)
        Assertions.assertEquals(countryEntity.carRegulations.carSigns, actual.carInfo.signs)
    }
}
