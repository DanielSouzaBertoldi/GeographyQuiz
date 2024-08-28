package daniel.bertoldi.geographyquiz.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import daniel.bertoldi.geographyquiz.domain.usecase.GetCountriesData
import daniel.bertoldi.geographyquiz.domain.usecase.GetCountriesDataUseCase
import daniel.bertoldi.geographyquiz.domain.usecase.GetFlagGameOptions
import daniel.bertoldi.geographyquiz.domain.usecase.GetFlagGameOptionsUseCase

@InstallIn(ViewModelComponent::class)
@Module
interface UseCaseModule {

    @Binds
    fun bindGetCountriesDataUseCase(getCountriesData: GetCountriesData): GetCountriesDataUseCase

    @Binds
    fun bindGetFlagGameOptionsUseCase(
        getFlagGameOptions: GetFlagGameOptions
    ): GetFlagGameOptionsUseCase
}