package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.geographyquiz.domain.model.HighScoreModel

interface HighScoreLocalDataSource {
    suspend fun saveGameScore(highScoreModel: HighScoreModel)
}