package daniel.bertoldi.geographyquiz.datasource

import daniel.bertoldi.database.dao.HighScoresDao
import daniel.bertoldi.geographyquiz.domain.mapper.HighScoreEntityToModelMapper
import daniel.bertoldi.geographyquiz.domain.mapper.HighScoreModelToEntityMapper
import daniel.bertoldi.geographyquiz.domain.model.HighScoreModel
import javax.inject.Inject

class HighScoreDefaultLocalDataSource @Inject constructor(
    private val highScoresDao: HighScoresDao,
    private val highScoreModelToEntityMapper: HighScoreModelToEntityMapper,
    private val highScoreEntityToModelMapper: HighScoreEntityToModelMapper,
) : HighScoreLocalDataSource {

    override suspend fun saveGameScore(highScoreModel: HighScoreModel) {
        val highScoreEntity = highScoreModelToEntityMapper.mapFrom(highScoreModel)
        highScoresDao.insertHighScore(highScoreEntity)
    }

    override suspend fun getHighScoresForGame(
        region: String,
        subRegion: String,
        gameMode: String,
    ): List<HighScoreModel> {
        val topFiveHighScores = highScoresDao.getHighScoresForCurrentGame(region, subRegion, gameMode)
        return highScoreEntityToModelMapper.mapFrom(topFiveHighScores)
    }
}