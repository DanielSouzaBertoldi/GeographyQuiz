package daniel.bertoldi.geographyquiz.domain.usecase

import daniel.bertoldi.geographyquiz.domain.model.HighScoreModel
import daniel.bertoldi.geographyquiz.domain.repository.HighScoreRepository
import daniel.bertoldi.geographyquiz.presentation.viewmodel.GameState
import javax.inject.Inject

class SaveUserScore @Inject constructor(
    private val highScoreRepository: HighScoreRepository,
) : SaveUserScoreUseCase {
    override suspend fun invoke(gameState: GameState) {
        highScoreRepository.saveGameScore(
            HighScoreModel(
                gameMode = gameState.gameMode,
                score = gameState.score,
                accuracy = gameState.roundState.accuracy.toFloat(),
                hits = gameState.roundState.hits,
                misses = gameState.roundState.misses,
                timeElapsed = gameState.duration,
                dateEpoch = System.currentTimeMillis(),
            )
        )
    }
}