package daniel.bertoldi.database

import daniel.bertoldi.database.entities.CountryEntity
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