package daniel.bertoldi.geographyquiz.presentation.model

import kotlin.time.Duration

data class HighScoresUIModel(
    val score: Int,
    val accuracy: Float,
    val hits: Int,
    val misses: Int,
    val timeElapsed: Duration?,
    val datePlayed: String,
)
