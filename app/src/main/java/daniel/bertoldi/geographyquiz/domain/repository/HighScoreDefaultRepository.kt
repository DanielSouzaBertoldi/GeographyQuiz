package daniel.bertoldi.geographyquiz.domain.repository

import daniel.bertoldi.geographyquiz.datasource.HighScoreLocalDataSource
import daniel.bertoldi.geographyquiz.domain.model.HighScoreModel
import javax.inject.Inject

class HighScoreDefaultRepository @Inject constructor(
    private val highScoreLocalDataSource: HighScoreLocalDataSource,
) : HighScoreRepository {
    override suspend fun saveGameScore(highScoreModel: HighScoreModel) {
        highScoreLocalDataSource.saveGameScore(highScoreModel)
    }

    override suspend fun getHighScoresForGame(
        region: String,
        subRegion: String,
        gameMode: String,
    ) = highScoreLocalDataSource.getHighScoresForGame(region, subRegion, gameMode)
}