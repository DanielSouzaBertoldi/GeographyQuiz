package daniel.bertoldi.database

import daniel.bertoldi.network.InternationalDialResponse
import daniel.bertoldi.network.NameDataResponse

object CountryEntityFactory {

    // TODO: create a test-utils module and put TestUtils in there so I can use its methods here
    fun make(
        countryCode: String = "BR",
        name: NameDataResponse = NameDataResponse("Brasil", "República do Brasil"),
        tld: List<String>? = null,
        independent: Boolean = true,
        unMember: Boolean = true,
        idd: InternationalDialResponse = InternationalDialResponse(root = "+5", suffixes = listOf("5")),
        capital: List<String>? = null,
        altSpellings: List<String> = emptyList(),
        region: String = "Americas",
        subRegion: String? = "South America",
        languages: Map<String, String>? = emptyMap(),
        landlocked: Boolean = false,
        area: Float = 1000F,
        emojiFlag: String = "br.png",
        population: Int = 20000,
        carSigns: List<String>? = emptyList(),
        carSide: String = "right",
        timezones: List<String> = emptyList(),
        continents: List<String> = emptyList(),
        flagPng: String = "flag.png",
        coatOfArms: String? = null,
        startOfWeek: String = "monday"
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
}