package daniel.bertoldi.geographyquiz.fakemodules

import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn
import daniel.bertoldi.geographyquiz.datasource.CountriesApi
import daniel.bertoldi.geographyquiz.di.CountriesApiModule
import daniel.bertoldi.network.BaseCountryDataResponseFactory
import daniel.bertoldi.network.BaseCountryDataResponse
import retrofit2.Response

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [CountriesApiModule::class]
)
object CountriesApiFakeModule {

    @Provides
    fun provideFakeCountriesApi(): CountriesApi = FakeCountriesApi()
}

class FakeCountriesApi : CountriesApi {
    override suspend fun getCountries(): Response<List<BaseCountryDataResponse>> {
        return Response.success(
            listOf(
                daniel.bertoldi.network.BaseCountryDataResponseFactory.make(
                    region = "Africa",
                    flags = daniel.bertoldi.network.BaseCountryDataResponseFactory.makeFlagsResponse(
                        png = "https://flagcdn.com/w320/sl.png",
                    ),
                ),
                daniel.bertoldi.network.BaseCountryDataResponseFactory.make(
                    region = "Africa",
                    flags = daniel.bertoldi.network.BaseCountryDataResponseFactory.makeFlagsResponse(
                        png = "https://flagcdn.com/w320/sl.png",
                    ),
                ),
                daniel.bertoldi.network.BaseCountryDataResponseFactory.make(
                    region = "Africa",
                    flags = daniel.bertoldi.network.BaseCountryDataResponseFactory.makeFlagsResponse(
                        png = "https://flagcdn.com/w320/sl.png",
                    ),
                ),
                daniel.bertoldi.network.BaseCountryDataResponseFactory.make(
                    region = "Africa",
                    flags = daniel.bertoldi.network.BaseCountryDataResponseFactory.makeFlagsResponse(
                        png = "https://flagcdn.com/w320/sl.png",
                    ),
                ),
                daniel.bertoldi.network.BaseCountryDataResponseFactory.make(
                    region = "Africa",
                    flags = daniel.bertoldi.network.BaseCountryDataResponseFactory.makeFlagsResponse(
                        png = "https://flagcdn.com/w320/sl.png",
                    ),
                ),
            )
        )
    }
}