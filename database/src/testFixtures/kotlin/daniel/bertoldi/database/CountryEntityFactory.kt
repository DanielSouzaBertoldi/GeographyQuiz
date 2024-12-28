package daniel.bertoldi.database

import daniel.bertoldi.database.entities.CarRegulations
import daniel.bertoldi.database.entities.CountryEntity
import daniel.bertoldi.database.entities.CountryNames
import daniel.bertoldi.database.entities.InternationalDialInfo
import daniel.bertoldi.utilities.test.utils.DayOfWeek
import daniel.bertoldi.utilities.test.utils.randomBoolean
import daniel.bertoldi.utilities.test.utils.randomEnumValue
import daniel.bertoldi.utilities.test.utils.randomFloat
import daniel.bertoldi.utilities.test.utils.randomInt
import daniel.bertoldi.utilities.test.utils.randomList
import daniel.bertoldi.utilities.test.utils.randomString
import daniel.bertoldi.utilities.test.utils.randomUUID
import daniel.bertoldi.utilities.test.utils.randomUrl

object CountryEntityFactory {
    fun make(
        countryCode: String = randomUUID(),
        name: CountryNames = makeCountryNames(),
        tld: List<String>? = randomList(),
        independent: Boolean = randomBoolean(),
        unMember: Boolean = randomBoolean(),
        idd: InternationalDialInfo = makeInternationalDialInfo(),
        capital: List<String>? = randomList(),
        altSpellings: List<String> = emptyList(),
        region: String = randomString(),
        subRegion: String? = randomString(),
        languages: Map<String, String>? = emptyMap(),
        landlocked: Boolean = randomBoolean(),
        area: Float = randomFloat(),
        emojiFlag: String = randomUrl(),
        population: Int = randomInt(),
        carRegulations: CarRegulations = makeCarRegulations(),
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
        carRegulations = carRegulations,
        timezones = timezones,
        continents = continents,
        flagPng = flagPng,
        coatOfArms = coatOfArms,
        startOfWeek = startOfWeek
    )

    fun makeCountryNames(
        common: String = randomString(),
        official: String = randomString(),
    ) = CountryNames(
        common = common,
        official = official,
    )

    fun makeInternationalDialInfo(
        root: String? = randomString(),
        suffixes: List<String>? = randomList()
    ) = InternationalDialInfo(
        root = root,
        suffixes = suffixes,
    )

    fun makeCarRegulations(
        carSigns: List<String> = randomList<String>(),
        carSide: String = randomString(),
    ) = CarRegulations(
        carSigns = carSigns,
        carSide = carSide,
    )
}
