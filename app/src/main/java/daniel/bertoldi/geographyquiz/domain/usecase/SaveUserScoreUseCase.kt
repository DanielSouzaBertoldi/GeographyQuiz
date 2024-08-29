package daniel.bertoldi.geographyquiz.domain.usecase

import daniel.bertoldi.geographyquiz.presentation.viewmodel.GameState

interface SaveUserScoreUseCase {
    suspend operator fun invoke(gameState: GameState)
}