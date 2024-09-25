package daniel.bertoldi.geographyquiz.domain.usecase

import daniel.bertoldi.geographyquiz.domain.repository.HighScoreRepository
import daniel.bertoldi.geographyquiz.presentation.mapper.HighScoresModelToUiMapper
import javax.inject.Inject

class FetchUserHighScoresForGame @Inject constructor(
    private val highScoreRepository: HighScoreRepository,
    private val HighScoresModelToUiMapper: HighScoresModelToUiMapper,
) : FetchUserHighScoresForGameUseCase {
    override suspend fun invoke(
        region: String,
        subRegion: String,
        gameMode: String,
    ) = HighScoresModelToUiMapper.mapFrom(
        highScoreRepository.getHighScoresForGame(region, subRegion, gameMode)
    )
}