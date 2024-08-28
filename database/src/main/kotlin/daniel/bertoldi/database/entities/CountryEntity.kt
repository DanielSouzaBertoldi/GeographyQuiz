package daniel.bertoldi.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey @ColumnInfo(name = "country_code") val countryCode: String,
    val name: CountryNames,
    @ColumnInfo(name = "top_level_domains") val tld: List<String>?,
    val independent: Boolean?,
    @ColumnInfo(name = "un_member") val unMember: Boolean,
    @ColumnInfo(name = "international_dial_info") val idd: InternationalDialInfo,
    val capital: List<String>?,
    @ColumnInfo(name = "alternative_spellings") val altSpellings: List<String>,
    val region: String,
    val subRegion: String?,
    val languages: Map<String, String>?,
    val landlocked: Boolean,
    val area: Float,
    @ColumnInfo(name = "emoji_flag") val emojiFlag: String,
    val population: Int,
    @ColumnInfo(name = "car_regulations") val carRegulations: CarRegulations,
    val timezones: List<String>,
    val continents: List<String>,
    val flagPng: String,
    val coatOfArms: String?,
    val startOfWeek: String, // TODO: could be enum?
)

// TODO: should these be tables? Not sure.
data class CountryNames(
    val common: String,
    val official: String,
)

data class InternationalDialInfo(
    val root: String?,
    val suffixes: List<String>?,
)

data class CarRegulations(
    val carSigns: List<String>?,
    val carSide: String,
)
