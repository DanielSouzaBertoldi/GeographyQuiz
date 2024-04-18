package daniel.bertoldi.geographyquiz.domain.model

data class CountryModel(
    val countryCode: String,
    val name: CountryNameModel,
    val topLevelDomains: List<String>?,
    val independent: Boolean?,
    val unMember: Boolean,
    val internationalDialResponse: CountryCallingCodesModel,
    val capital: List<String>?,
    val altSpellings: List<String>,
    val region: String,
    val subRegion: String?,
    val languages: Map<String, String>?,
    val landlocked: Boolean,
    val area: Float,
    val emojiFlag: String,
    val population: Int,
    val carInfo: CountryCarInfoModel,
    val timezones: List<String>,
    val continents: List<String>,
    val flagPng: String,
    val coatOfArms: String?,
    val startOfWeek: DayOfWeek,
)

data class CountryNameModel(
    val common: String,
    val official: String,
)

data class CountryCallingCodesModel(
    val root: String?,
    val suffixes: List<String>?,
)

data class CountryCarInfoModel(
    val signs: List<String>?,
    val side: String,
)
