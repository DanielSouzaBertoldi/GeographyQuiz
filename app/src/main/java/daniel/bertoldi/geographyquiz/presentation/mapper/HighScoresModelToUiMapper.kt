package daniel.bertoldi.geographyquiz.presentation.mapper

import daniel.bertoldi.geographyquiz.domain.model.HighScoreModel
import daniel.bertoldi.geographyquiz.presentation.model.HighScoresUIModel

interface HighScoresModelToUiMapper {
    suspend fun mapFrom(highScores: List<HighScoreModel>): List<HighScoresUIModel>
}