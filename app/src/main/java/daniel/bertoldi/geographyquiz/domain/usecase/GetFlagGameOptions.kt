package daniel.bertoldi.geographyquiz.domain.usecase

import android.content.Context
import coil.imageLoader
import coil.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import daniel.bertoldi.geographyquiz.domain.repository.CountriesRepository
import daniel.bertoldi.geographyquiz.presentation.mapper.CountryModelToCountryFlagUiMapper
import daniel.bertoldi.geographyquiz.presentation.model.CountryFlagUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetFlagGameOptions @Inject constructor(
    private val countriesRepository: CountriesRepository,
    private val countryModelToCountryFlagUiMapper: CountryModelToCountryFlagUiMapper,
    @ApplicationContext private val context: Context,
): GetFlagGameOptionsUseCase {
    override suspend fun invoke(
        chosenRegion: String,
        chosenSubRegion: String,
    ): Flow<List<CountryFlagUi>> {
        return countriesRepository.getCountriesInSubRegion(chosenRegion, chosenSubRegion).transform {
            val countriesUi = countryModelToCountryFlagUiMapper.mapFrom(it)
            preLoadFlags(countriesUi.map { country -> country.flagUrl })
            emit(countriesUi)
        }
    }

    // TODO: think of a way to prefetch low quality images for low-end devices.
    private suspend fun preLoadFlags(flagsUrl: List<String>) {
        withContext(Dispatchers.IO) {
            flagsUrl.forEach { flagUrl ->
                val request = ImageRequest.Builder(context)
                    .data(flagUrl)
                    .build()
                context.imageLoader.enqueue(request)
            }
        }
    }
}