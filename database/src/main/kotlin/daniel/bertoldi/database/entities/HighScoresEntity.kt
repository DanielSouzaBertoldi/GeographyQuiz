package daniel.bertoldi.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Duration

@Entity(tableName = "user_high_scores")
data class HighScoresEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "game_mode") val gameMode: String,
    val region: String,
    @ColumnInfo(name = "sub_region") val subRegion: String,
    val score: Int,
    val accuracy: Float,
    val hits: Int,
    val misses: Int,
    @ColumnInfo(name = "time_elapsed") val timeElapsedInMillis: Duration?,
    @ColumnInfo(name = "date_played") val dateEpochInMillis: Long,
)
