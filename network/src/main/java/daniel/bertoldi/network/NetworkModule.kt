package daniel.bertoldi.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val REST_COUNTRIES_URL = "https://restcountries.com/v3.1/"


@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().apply {
        addLast(KotlinJsonAdapterFactory())
    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(
        moshi: Moshi,
    ): Retrofit = Retrofit.Builder().apply {
        baseUrl(REST_COUNTRIES_URL)
        addConverterFactory(MoshiConverterFactory.create(moshi))
    }.build()
}
