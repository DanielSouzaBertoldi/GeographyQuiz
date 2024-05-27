package daniel.bertoldi.geographyquiz.domain.usecase

import daniel.bertoldi.geographyquiz.presentation.model.CountryFlagUi
import kotlinx.coroutines.flow.Flow

interface GetFlagGameOptionsUseCase {
    suspend operator fun invoke(
        chosenRegion: String,
        chosenSubRegion: String,
    ): Flow<List<CountryFlagUi>>
}