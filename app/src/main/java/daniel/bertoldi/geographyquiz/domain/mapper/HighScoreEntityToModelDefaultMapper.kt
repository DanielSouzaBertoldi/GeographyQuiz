package daniel.bertoldi.geographyquiz.domain.mapper

import daniel.bertoldi.database.entities.HighScoresEntity
import daniel.bertoldi.geographyquiz.domain.model.HighScoreModel
import daniel.bertoldi.geographyquiz.presentation.model.GameMode.Companion.toGameMode
import javax.inject.Inject

class HighScoreEntityToModelDefaultMapper @Inject constructor() : HighScoreEntityToModelMapper {
    override fun mapFrom(highScoresEntities: List<HighScoresEntity>): List<HighScoreModel> {
        return highScoresEntities.map {
            HighScoreModel(
                gameMode = it.gameMode.toGameMode(),
                score = it.score,
                accuracy = it.accuracy,
                hits = it.hits,
                misses = it.misses,
                timeElapsed = it.timeElapsedInMillis,
                dateEpoch = it.dateEpochInMillis,
            )
        }
    }
}