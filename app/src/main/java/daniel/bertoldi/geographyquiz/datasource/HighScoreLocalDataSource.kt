package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.geographyquiz.domain.model.HighScoreModel

interface HighScoreLocalDataSource {
    suspend fun saveGameScore(highScoreModel: HighScoreModel)
    suspend fun getHighScoresForGame(
        region: String,
        subRegion: String,
        gameMode: String,
    ): List<HighScoreModel>
}