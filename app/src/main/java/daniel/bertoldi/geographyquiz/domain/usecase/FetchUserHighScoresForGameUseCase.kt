package daniel.bertoldi.geographyquiz.domain.usecase

import daniel.bertoldi.geographyquiz.presentation.model.HighScoresUIModel

interface FetchUserHighScoresForGameUseCase {
    suspend operator fun invoke(
        region: String,
        subRegion: String,
        gameMode: String,
    ): List<HighScoresUIModel>
}