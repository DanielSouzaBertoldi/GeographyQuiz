package daniel.bertoldi.network

import daniel.bertoldi.utilities.test.utils.DayOfWeek
import daniel.bertoldi.utilities.test.utils.randomBoolean
import daniel.bertoldi.utilities.test.utils.randomEnumValue
import daniel.bertoldi.utilities.test.utils.randomList
import daniel.bertoldi.utilities.test.utils.randomString
import daniel.bertoldi.utilities.test.utils.randomUrl
import kotlin.random.Random

object BaseCountryDataResponseFactory {

    fun make(
        name: NameDataResponse = makeCountryName(),
        topLevelDomains: List<String>? = randomList(),
        countryCode: String = randomString(),
        independent: Boolean? = randomBoolean(),
        unMember: Boolean = randomBoolean(),
        idd: InternationalDialResponse = makeInternationalDialResponse(),
        capital: List<String>? = randomList(),
        alternativeSpellings: List<String> = randomList(),
        region: String = randomString(),
        subRegion: String? = randomString(),
        languages: Map<String, String>? = mapOf(randomString() to randomString()),
        translations: SupportedLanguagesResponse = makeSupportedLanguagesResponse(),
        landlocked: Boolean = randomBoolean(),
        area: Float = Random.nextFloat(),
        emojiFlag: String = randomString(),
        population: Int = Random.nextInt(),
        car: CarResponse = makeCarResponse(),
        timezones: List<String> = randomList(),
        continents: List<String> = randomList(),
        flags: FlagsResponse = makeFlagsResponse(),
        coatOfArms: CoatOfArmsResponse = makeCoatOfArmsResponse(),
        startOfWeek: String = randomEnumValue<DayOfWeek>().name,
    ) = BaseCountryDataResponse(
        name = name,
        topLevelDomains = topLevelDomains,
        countryCode = countryCode,
        independent = independent,
        unMember = unMember,
        idd = idd,
        capital = capital,
        alternativeSpellings = alternativeSpellings,
        region = region,
        subRegion = subRegion,
        languages = languages,
        translations = translations,
        landlocked = landlocked,
        area = area,
        emojiFlag = emojiFlag,
        population = population,
        car = car,
        timezones = timezones,
        continents = continents,
        flags = flags,
        coatOfArms = coatOfArms,
        startOfWeek = startOfWeek,
    )

    fun makeCountryName(
        common: String = randomString(),
        official: String = randomString(),
    ) = NameDataResponse(
        common = common,
        official = official,
    )

    fun makeInternationalDialResponse(
        root: String? = randomString(),
        suffixes: List<String>? = randomList(),
    ) = InternationalDialResponse(
        root = root,
        suffixes = suffixes,
    )

    fun makeSupportedLanguagesResponse(
        fra: NameDataResponse = makeCountryName(),
        ita: NameDataResponse = makeCountryName(),
        por: NameDataResponse = makeCountryName(),
        spa: NameDataResponse = makeCountryName(),
    ) = SupportedLanguagesResponse(
        fra = fra,
        ita = ita,
        por = por,
        spa = spa,
    )

    fun makeCarResponse(
        signs: List<String>? = randomList(),
        side: String = randomString(),
    ) = CarResponse(
        signs = signs,
        side = side,
    )

    fun makeFlagsResponse(
        png: String = randomUrl(),
        alt: String? = randomString(),
    ) = FlagsResponse(
        png = png,
        alt = alt,
    )

    fun makeCoatOfArmsResponse(
        png: String = randomUrl(),
    ) = CoatOfArmsResponse(
        png = png,
    )
}