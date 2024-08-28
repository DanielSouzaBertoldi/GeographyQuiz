package daniel.bertoldi.geographyquiz.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import daniel.bertoldi.geographyquiz.domain.mapper.BaseCountryDataResponseToModelDefaultMapper
import daniel.bertoldi.geographyquiz.domain.mapper.BaseCountryDataResponseToModelMapper
import daniel.bertoldi.geographyquiz.domain.mapper.CountryEntityToModelDefaultMapper
import daniel.bertoldi.geographyquiz.domain.mapper.CountryEntityToModelMapper
import daniel.bertoldi.geographyquiz.domain.mapper.CountryModelToEntityDefaultMapper
import daniel.bertoldi.geographyquiz.domain.mapper.CountryModelToEntityMapper
import daniel.bertoldi.geographyquiz.domain.mapper.HighScoreEntityToModelDefaultMapper
import daniel.bertoldi.geographyquiz.domain.mapper.HighScoreEntityToModelMapper
import daniel.bertoldi.geographyquiz.domain.mapper.HighScoreModelToEntityDefaultMapper
import daniel.bertoldi.geographyquiz.domain.mapper.HighScoreModelToEntityMapper
import daniel.bertoldi.geographyquiz.presentation.mapper.CountryModelToCountryFlagUiDefaultMapper
import daniel.bertoldi.geographyquiz.presentation.mapper.CountryModelToCountryFlagUiMapper

@InstallIn(ViewModelComponent::class)
@Module
interface MappersModule {

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
    fun bindCountryModelToFlagUiMapper(
        countryModelToCountryFlagUiDefaultMapper: CountryModelToCountryFlagUiDefaultMapper,
    ): CountryModelToCountryFlagUiMapper

    @Binds
    fun bindHighScoreModelToEntityMapper(
        highScoreModelToEntityDefaultMapper: HighScoreModelToEntityDefaultMapper,
    ): HighScoreModelToEntityMapper

    @Binds
    fun bindHighScoreEntityToModelMapper(
        highScoreEntityToModelDefaultMapper: HighScoreEntityToModelDefaultMapper,
    ): HighScoreEntityToModelMapper
}