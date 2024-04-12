package daniel.bertoldi.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseCountryDataResponse(
    val name: NameDataResponse,
    @Json(name = "tld") val topLevelDomains: List<String>?,
    @Json(name = "cca2") val countryCode: String,
    val independent: Boolean?,
    val unMember: Boolean,
    val idd: InternationalDialResponse,
    val capital: List<String>?,
    @Json(name = "altSpellings") val alternativeSpellings: List<String>,
    val region: String,
    @Json(name = "subregion") val subRegion: String?,
    val languages: Map<String, String>?,
    val translations: SupportedLanguagesResponse, // TODO: not sure if this is needed
    val landlocked: Boolean,
    val area: Float,
    @Json(name = "flag") val emojiFlag: String,
    val population: Int,
    val car: CarResponse,
    val timezones: List<String>,
    val continents: List<String>,
    val flags: FlagsResponse,
    val coatOfArms: CoatOfArmsResponse,
    val startOfWeek: String,
)

@JsonClass(generateAdapter = true)
data class NameDataResponse(
    val common: String,
    val official: String,
    // val nativeName: Map<String, Yeah>?, TODO: check if it's worth it to store the nativeName
)

@JsonClass(generateAdapter = true)
data class InternationalDialResponse(
    val root: String?,
    val suffixes: List<String>?,
)

@JsonClass(generateAdapter = true)
data class SupportedLanguagesResponse( // TODO: maybe this doesn't make sense. Idk.
    val fra: NameDataResponse,
    val ita: NameDataResponse,
    val por: NameDataResponse,
    val spa: NameDataResponse,
)

@JsonClass(generateAdapter = true)
data class CarResponse(
    val signs: List<String>?,
    val side: String,
)

@JsonClass(generateAdapter = true)
data class FlagsResponse(
    val png: String,
    val alt: String?,
)

@JsonClass(generateAdapter = true)
data class CoatOfArmsResponse(
    val png: String?,
)