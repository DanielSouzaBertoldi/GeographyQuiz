package daniel.bertoldi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Test(
    @PrimaryKey val uuid: Int,
    @ColumnInfo(name = "country_name") val countryName: String,
    val population: String,
)