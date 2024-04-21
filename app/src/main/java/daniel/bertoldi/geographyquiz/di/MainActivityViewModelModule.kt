package daniel.bertoldi.geographyquiz.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import daniel.bertoldi.geographyquiz.datasource.CountriesDefaultLocalDataSource
import daniel.bertoldi.geographyquiz.datasource.CountriesDefaultRemoteDataSource
import daniel.bertoldi.geographyquiz.datasource.CountriesLocalDataSource
import daniel.bertoldi.geographyquiz.datasource.CountriesRemoteDataSource
import daniel.bertoldi.geographyquiz.domain.mapper.BaseCountryDataResponseToModelDefaultMapper
import daniel.bertoldi.geographyquiz.domain.mapper.BaseCountryDataResponseToModelMapper
import daniel.bertoldi.geographyquiz.domain.mapper.CountryEntityToModelDefaultMapper
import daniel.bertoldi.geographyquiz.domain.mapper.CountryEntityToModelMapper
import daniel.bertoldi.geographyquiz.domain.repository.CountriesDefaultRepository
import daniel.bertoldi.geographyquiz.domain.repository.CountriesRepository
import daniel.bertoldi.geographyquiz.domain.usecase.GetCountriesData
import daniel.bertoldi.geographyquiz.domain.usecase.GetCountriesDataUseCase

private const val USER_PREFS = "user_preferences"

@InstallIn(ViewModelComponent::class)
@Module
interface MainActivityViewModelModule {

    @Binds
    fun bindGetCountriesDataUseCase(getCountriesData: GetCountriesData): GetCountriesDataUseCase

    @Binds
    fun bindCountryRepository(countriesRepository: CountriesDefaultRepository): CountriesRepository

    @Binds
    fun bindCountryRemoteDataSource(
        countryRemoteDataSource: CountriesDefaultRemoteDataSource,
    ): CountriesRemoteDataSource

    @Binds
    fun bindCountryLocalDataSource(
        countryLocalDataSource: CountriesDefaultLocalDataSource,
    ): CountriesLocalDataSource

    @Binds
    fun bindCountryEntityToModelMapper(
        countryEntityToModelDefaultMapper: CountryEntityToModelDefaultMapper,
    ): CountryEntityToModelMapper

    @Binds
    fun bindBaseCountryDataResponseToModelMapper(
        countryDataResponseToModelDefaultMapper: BaseCountryDataResponseToModelDefaultMapper,
    ): BaseCountryDataResponseToModelMapper
}