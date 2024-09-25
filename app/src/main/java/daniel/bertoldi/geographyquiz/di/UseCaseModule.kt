package daniel.bertoldi.geographyquiz.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import daniel.bertoldi.geographyquiz.domain.usecase.FetchUserHighScoresForGame
import daniel.bertoldi.geographyquiz.domain.usecase.FetchUserHighScoresForGameUseCase
import daniel.bertoldi.geographyquiz.domain.usecase.GetCountriesData
import daniel.bertoldi.geographyquiz.domain.usecase.GetCountriesDataUseCase
import daniel.bertoldi.geographyquiz.domain.usecase.GetFlagGameOptions
import daniel.bertoldi.geographyquiz.domain.usecase.GetFlagGameOptionsUseCase
import daniel.bertoldi.geographyquiz.domain.usecase.SaveUserScore
import daniel.bertoldi.geographyquiz.domain.usecase.SaveUserScoreUseCase

@InstallIn(ViewModelComponent::class)
@Module
interface UseCaseModule {

    @Binds
    fun bindGetCountriesDataUseCase(getCountriesData: GetCountriesData): GetCountriesDataUseCase

    @Binds
    fun bindGetFlagGameOptionsUseCase(
        getFlagGameOptions: GetFlagGameOptions
    ): GetFlagGameOptionsUseCase

    @Binds
    fun bindSaveUserScoreUseCase(
        saveUserScore: SaveUserScore
    ): SaveUserScoreUseCase

    @Binds
    fun bindFetchUserHighScoresForGameUseCase(
        fetchUserHighScoresForGame: FetchUserHighScoresForGame,
    ): FetchUserHighScoresForGameUseCase
}