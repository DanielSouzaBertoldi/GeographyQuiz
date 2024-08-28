package daniel.bertoldi.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import daniel.bertoldi.database.entities.HighScoresEntity

@Dao
interface HighScoresDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertHighScore(highScore: HighScoresEntity)

    @Query("DELETE FROM user_high_scores")
    suspend fun deleteAllHighScores()

    @Query("SELECT * FROM user_high_scores WHERE game_mode = :gameMode ORDER BY accuracy DESC, time_elapsed DESC LIMIT 10")
    suspend fun getHighScoresForGameMode(gameMode: String): List<HighScoresEntity>
}