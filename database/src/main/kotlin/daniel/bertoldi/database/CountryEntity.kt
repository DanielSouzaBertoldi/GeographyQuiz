package daniel.bertoldi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import daniel.bertoldi.network.NameDataResponse
import daniel.bertoldi.network.InternationalDialResponse

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey @ColumnInfo(name = "contry_code") val countryCode: String,
    val name: NameDataResponse,
    @ColumnInfo(name = "top_level_domains") val tld: List<String>?,
    val independent: Boolean,
    @ColumnInfo(name = "un_member") val unMember: Boolean,
    @ColumnInfo(name = "international_dial_response") val idd: InternationalDialResponse,
    val capital: List<String>?,
    @ColumnInfo(name = "alternative_spellings") val altSpellings: List<String>,
    val region: String,
    val subRegion: String?,
    val languages: Map<String, String>?,
    val landlocked: Boolean,
    val area: Float,
    @ColumnInfo(name = "emoji_flag") val emojiFlag: String,
    val population: Int,
    @ColumnInfo(name = "car_signs") val carSigns: List<String>?, // TODO: group both parameters into one model?
    @ColumnInfo(name = "car_side") val carSide: String,
    val timezones: List<String>,
    val continents: List<String>,
    val flagPng: String,
    val coatOfArms: String?,
    val startOfWeek: String, // TODO: could be enum?
)
