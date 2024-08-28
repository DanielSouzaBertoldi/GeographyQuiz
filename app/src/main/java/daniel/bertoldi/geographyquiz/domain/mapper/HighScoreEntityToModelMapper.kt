package daniel.bertoldi.geographyquiz.domain.mapper

import daniel.bertoldi.database.entities.HighScoresEntity
import daniel.bertoldi.geographyquiz.domain.model.HighScoreModel

interface HighScoreEntityToModelMapper {
    fun mapFrom(highScoresEntities: List<HighScoresEntity>): List<HighScoreModel>
}