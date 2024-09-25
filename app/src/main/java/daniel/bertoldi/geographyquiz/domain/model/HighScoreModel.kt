package daniel.bertoldi.geographyquiz.domain.model

import daniel.bertoldi.geographyquiz.presentation.model.GameMode
import kotlin.time.Duration

data class HighScoreModel(
    val gameMode: GameMode,
    val region: String,
    val subRegion: String,
    val score: Int,
    val accuracy: Float,
    val hits: Int,
    val misses: Int,
    val timeElapsed: Duration?,
    val dateEpoch: Long,
)
