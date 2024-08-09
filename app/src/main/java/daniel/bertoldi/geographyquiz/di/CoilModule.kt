package daniel.bertoldi.geographyquiz.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.util.DebugLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class CoilModule {

    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("geography_quiz_image_cache"))
                    .maxSizePercent(0.1)
                    .build()
            }
            .respectCacheHeaders(enable = false) // TODO: maybe i don't need this. Check further.
            .crossfade(enable = true)
            .logger(DebugLogger())
            .build()
    }
}