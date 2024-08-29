package daniel.bertoldi.geographyquiz.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import daniel.bertoldi.geographyquiz.domain.repository.CountriesDefaultRepository
import daniel.bertoldi.geographyquiz.domain.repository.CountriesRepository
import daniel.bertoldi.geographyquiz.domain.repository.HighScoreDefaultRepository
import daniel.bertoldi.geographyquiz.domain.repository.HighScoreRepository

@InstallIn(ViewModelComponent::class)
@Module
interface RepositoriesModule {
    @Binds
    fun bindCountryRepository(countriesRepository: CountriesDefaultRepository): CountriesRepository

    @Binds
    fun bindHighScoreRepository(
        highScoreDefaultRepository: HighScoreDefaultRepository
    ): HighScoreRepository
}