package daniel.bertoldi.geographyquiz.domain.mapper

import daniel.bertoldi.database.entities.HighScoresEntity
import daniel.bertoldi.geographyquiz.domain.model.HighScoreModel
import javax.inject.Inject

class HighScoreModelToEntityDefaultMapper @Inject constructor() : HighScoreModelToEntityMapper {
    override fun mapFrom(highScoreModel: HighScoreModel): HighScoresEntity {
        return HighScoresEntity(
            gameMode = highScoreModel.gameMode.name,
            region = highScoreModel.region,
            subRegion = highScoreModel.subRegion,
            score = highScoreModel.score,
            accuracy = highScoreModel.accuracy,
            hits = highScoreModel.hits,
            misses = highScoreModel.misses,
            timeElapsedInMillis = highScoreModel.timeElapsed,
            dateEpochInMillis = highScoreModel.dateEpoch,
        )
    }
}