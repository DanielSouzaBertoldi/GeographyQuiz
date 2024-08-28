package daniel.bertoldi.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_high_scores")
data class HighScoresEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "game_mode") val gameMode: String,
    val score: Int,
    val accuracy: Float,
    val hits: Int,
    val misses: Int,
    @ColumnInfo(name = "time_elapsed") val timeElapsed: Long?,
    val date: Long,
)
