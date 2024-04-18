package daniel.bertoldi.geographyquiz.domain.mapper

import daniel.bertoldi.database.CountryEntity
import daniel.bertoldi.network.InternationalDialResponse
import daniel.bertoldi.network.NameDataResponse
import daniel.bertoldi.test.utils.DayOfWeek
import daniel.bertoldi.test.utils.randomBoolean
import daniel.bertoldi.test.utils.randomEnumValue
import daniel.bertoldi.test.utils.randomFloat
import daniel.bertoldi.test.utils.randomInt
import daniel.bertoldi.test.utils.randomList
import daniel.bertoldi.test.utils.randomString
import daniel.bertoldi.test.utils.randomUUID
import daniel.bertoldi.test.utils.randomUrl
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
        val countryName = CountryEntityFactory.makeNameDataResponse()
        val countryEntity = CountryEntityFactory.make(name = countryName)
        val actual = mapper.mapFrom(listOf(countryEntity)).first()

        Assertions.assertEquals(countryEntity.name.common, actual.name.common)
        Assertions.assertEquals(countryEntity.name.official, actual.name.official)
    }

    @Test
    fun mapFrom_withCountryEntity_assertCountryCallingCodesCorrectlyMapped() {
        val countryIdd = CountryEntityFactory.makeInternationalDialResponse()
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
            carSigns = carSigns,
            carSide = carSide,
        )
        val actual = mapper.mapFrom(listOf(countryEntity)).first()

        Assertions.assertEquals(countryEntity.carSide, actual.carInfo.side)
        Assertions.assertEquals(countryEntity.carSigns, actual.carInfo.signs)
    }
}


// TODO: Move the CountryEntityFactory to a common module that can
//  be used here and in the `androidTest` in :database, then delete this duplicate code.
object CountryEntityFactory {

    fun make(
        countryCode: String = randomUUID(),
        name: NameDataResponse = makeNameDataResponse(),
        tld: List<String>? = randomList(),
        independent: Boolean = randomBoolean(),
        unMember: Boolean = randomBoolean(),
        idd: InternationalDialResponse = makeInternationalDialResponse(),
        capital: List<String>? = randomList(),
        altSpellings: List<String> = emptyList(),
        region: String = randomString(),
        subRegion: String? = randomString(),
        languages: Map<String, String>? = emptyMap(),
        landlocked: Boolean = randomBoolean(),
        area: Float = randomFloat(),
        emojiFlag: String = randomUrl(),
        population: Int = randomInt(),
        carSigns: List<String>? = randomList(),
        carSide: String = randomString(),
        timezones: List<String> = randomList(),
        continents: List<String> = randomList(),
        flagPng: String = randomUrl(),
        coatOfArms: String? = randomUrl(),
        startOfWeek: String = randomEnumValue<DayOfWeek>().name,
    ) = CountryEntity(
        countryCode = countryCode,
        name = name,
        tld = tld,
        independent = independent,
        unMember = unMember,
        idd = idd,
        capital = capital,
        altSpellings = altSpellings,
        region = region,
        subRegion = subRegion,
        languages = languages,
        landlocked = landlocked,
        area = area,
        emojiFlag = emojiFlag,
        population = population,
        carSigns = carSigns,
        carSide = carSide,
        timezones = timezones,
        continents = continents,
        flagPng = flagPng,
        coatOfArms = coatOfArms,
        startOfWeek = startOfWeek
    )

    // TODO: this shouldn't be referencing a model in the network module.
    fun makeNameDataResponse(
        common: String = randomString(),
        official: String = randomString(),
    ) = NameDataResponse(
        common = common,
        official = official,
    )

    fun makeInternationalDialResponse(
        root: String? = randomString(),
        suffixes: List<String>? = randomList()
    ) = InternationalDialResponse(
        root = root,
        suffixes = suffixes,
    )
}