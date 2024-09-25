package daniel.bertoldi.geographyquiz.domain.repository

import daniel.bertoldi.geographyquiz.domain.model.HighScoreModel

interface HighScoreRepository {
    suspend fun saveGameScore(highScoreModel: HighScoreModel)
    suspend fun getHighScoresForGame(
        region: String,
        subRegion: String,
        gameMode: String,
    ): List<HighScoreModel>
}