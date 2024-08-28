package daniel.bertoldi.geographyquiz.domain.model

import daniel.bertoldi.geographyquiz.presentation.model.GameMode
import java.util.Date
import kotlin.time.Duration

data class HighScoreModel(
    val gameMode: GameMode,
    val score: Int,
    val accuracy: Float,
    val hits: Int,
    val misses: Int,
    val timeElapsed: Duration?,
    val date: Date,
)
