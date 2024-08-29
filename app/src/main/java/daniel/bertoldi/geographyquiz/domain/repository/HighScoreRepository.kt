package daniel.bertoldi.geographyquiz.domain.repository

import daniel.bertoldi.geographyquiz.domain.model.HighScoreModel

interface HighScoreRepository {
    suspend fun saveGameScore(highScoreModel: HighScoreModel)
}