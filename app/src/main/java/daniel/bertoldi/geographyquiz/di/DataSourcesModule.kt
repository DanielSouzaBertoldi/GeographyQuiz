package daniel.bertoldi.geographyquiz.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import daniel.bertoldi.geographyquiz.datasource.CountriesDefaultLocalDataSource
import daniel.bertoldi.geographyquiz.datasource.CountriesDefaultRemoteDataSource
import daniel.bertoldi.geographyquiz.datasource.CountriesLocalDataSource
import daniel.bertoldi.geographyquiz.datasource.CountriesRemoteDataSource
import daniel.bertoldi.geographyquiz.datasource.HighScoreDefaultLocalDataSource
import daniel.bertoldi.geographyquiz.datasource.HighScoreLocalDataSource

@Module
@InstallIn(ViewModelComponent::class)
interface DataSourcesModule {
    @Binds
    fun bindCountryRemoteDataSource(
        countryRemoteDataSource: CountriesDefaultRemoteDataSource
    ): CountriesRemoteDataSource

    @Binds
    fun bindCountryLocalDataSource(
        countryLocalDataSource: CountriesDefaultLocalDataSource
    ): CountriesLocalDataSource

    @Binds
    fun bindHighScoreLocalDataSource(
        highScoreDefaultLocalDataSource: HighScoreDefaultLocalDataSource
    ): HighScoreLocalDataSource
}