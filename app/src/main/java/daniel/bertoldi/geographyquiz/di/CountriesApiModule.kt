package daniel.bertoldi.geographyquiz.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import daniel.bertoldi.geographyquiz.datasource.CountriesApi
import retrofit2.Retrofit

@InstallIn(ViewModelComponent::class)
@Module
object CountriesApiModule {

    @Provides
    fun provideCountriesApi(
        retrofit: Retrofit,
    ): CountriesApi = retrofit.create(CountriesApi::class.java)
}