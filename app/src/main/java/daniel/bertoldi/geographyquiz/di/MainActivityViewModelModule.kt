package daniel.bertoldi.geographyquiz.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import daniel.bertoldi.geographyquiz.datasource.CountriesDefaultLocalDataSource
import daniel.bertoldi.geographyquiz.datasource.CountriesDefaultRemoteDataSource
import daniel.bertoldi.geographyquiz.datasource.CountriesLocalDataSource
import daniel.bertoldi.geographyquiz.datasource.CountriesRemoteDataSource
import daniel.bertoldi.geographyquiz.datastore.CountriesDataStore
import daniel.bertoldi.geographyquiz.datastore.CountriesDefaultDataStore
import daniel.bertoldi.geographyquiz.domain.mapper.BaseCountryDataResponseToModelDefaultMapper
import daniel.bertoldi.geographyquiz.domain.mapper.BaseCountryDataResponseToModelMapper
import daniel.bertoldi.geographyquiz.domain.mapper.CountryEntityToModelDefaultMapper
import daniel.bertoldi.geographyquiz.domain.mapper.CountryEntityToModelMapper
import daniel.bertoldi.geographyquiz.domain.mapper.CountryModelToEntityDefaultMapper
import daniel.bertoldi.geographyquiz.domain.mapper.CountryModelToEntityMapper
import daniel.bertoldi.geographyquiz.domain.repository.CountriesDefaultRepository
import daniel.bertoldi.geographyquiz.domain.repository.CountriesRepository
import daniel.bertoldi.geographyquiz.domain.usecase.GetCountriesData
import daniel.bertoldi.geographyquiz.domain.usecase.GetCountriesDataUseCase

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

    @Binds
    fun bindCountryModelToEntityMapper(
        countryModelToEntityDefaultMapper: CountryModelToEntityDefaultMapper,
    ): CountryModelToEntityMapper

    @Binds
    fun bindCountriesDataStore(
        countriesDefaultDataStore: CountriesDefaultDataStore,
    ): CountriesDataStore
}