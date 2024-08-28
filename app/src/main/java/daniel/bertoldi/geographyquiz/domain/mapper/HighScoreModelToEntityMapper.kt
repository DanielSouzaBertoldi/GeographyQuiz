package daniel.bertoldi.geographyquiz.domain.mapper

import daniel.bertoldi.database.entities.HighScoresEntity
import daniel.bertoldi.geographyquiz.domain.model.HighScoreModel

interface HighScoreModelToEntityMapper {
    fun mapFrom(highScoreModel: HighScoreModel): HighScoresEntity
}