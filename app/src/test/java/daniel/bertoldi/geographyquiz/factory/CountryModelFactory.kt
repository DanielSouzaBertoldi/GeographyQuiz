package daniel.bertoldi.geographyquiz.factory

import daniel.bertoldi.geographyquiz.domain.model.CountryCallingCodesModel
import daniel.bertoldi.geographyquiz.domain.model.CountryCarInfoModel
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import daniel.bertoldi.geographyquiz.domain.model.CountryNameModel
import daniel.bertoldi.geographyquiz.domain.model.DayOfWeek
import daniel.bertoldi.test.utils.randomBoolean
import daniel.bertoldi.test.utils.randomEnumValue
import daniel.bertoldi.test.utils.randomFloat
import daniel.bertoldi.test.utils.randomInt
import daniel.bertoldi.test.utils.randomList
import daniel.bertoldi.test.utils.randomString
import daniel.bertoldi.test.utils.randomUUID
import daniel.bertoldi.test.utils.randomUrl

object CountryModelFactory {

    fun makeList(
        numberOfItems: Int = randomInt(),
    ) = mutableListOf<CountryModel>().apply {
        repeat(numberOfItems) { add(make()) }
    }

    fun make(
        countryCode: String = randomUUID(),
        name: CountryNameModel = makeCountryNameModel(),
        topLevelDomains: List<String>? = randomList(),
        independent: Boolean? = randomBoolean(),
        unMember: Boolean = randomBoolean(),
        internationalDialResponse: CountryCallingCodesModel = makeCountryCallingCodesModel(),
        capital: List<String>? = randomList(),
        altSpellings: List<String> = randomList(),
        region: String = randomString(),
        subRegion: String? = randomString(),
        languages: Map<String, String>? = mapOf(randomString() to randomString()),
        landlocked: Boolean = randomBoolean(),
        area: Float = randomFloat(),
        emojiFlag: String = randomString(),
        population: Int = randomInt(),
        carInfo: CountryCarInfoModel = makeCountryCarInfoModel(),
        timezones: List<String> = randomList(),
        continents: List<String> = randomList(),
        flagPng: String = randomUrl(),
        coatOfArms: String? = randomUrl(),
        startOfWeek: DayOfWeek = randomEnumValue(),
    ) = CountryModel(
        countryCode = countryCode,
        name = name,
        topLevelDomains = topLevelDomains,
        independent = independent,
        unMember = unMember,
        internationalDialResponse = internationalDialResponse,
        capital = capital,
        altSpellings = altSpellings,
        region = region,
        subRegion = subRegion,
        languages = languages,
        landlocked = landlocked,
        area = area,
        emojiFlag = emojiFlag,
        population = population,
        carInfo = carInfo,
        timezones = timezones,
        continents = continents,
        flagPng = flagPng,
        coatOfArms = coatOfArms,
        startOfWeek = startOfWeek,
    )

    fun makeCountryNameModel(
        common: String = randomString(),
        official: String = randomString(),
    ) = CountryNameModel(
        common = common,
        official = official,
    )

    fun makeCountryCallingCodesModel(
        root: String = randomString(),
        suffixes: List<String>? = randomList(),
    ) = CountryCallingCodesModel(
        root = root,
        suffixes = suffixes,
    )

    fun makeCountryCarInfoModel(
        signs: List<String>? = randomList(),
        side: String = randomString(),
    ) = CountryCarInfoModel(
        signs = signs,
        side = side,
    )
}