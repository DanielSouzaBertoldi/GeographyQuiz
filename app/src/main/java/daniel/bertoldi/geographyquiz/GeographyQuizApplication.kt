package daniel.bertoldi.geographyquiz

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GeographyQuizApplication : Application(), ImageLoaderFactory {
    override fun newImageLoader() = ImageLoader.Builder(this)
        .memoryCache {
            MemoryCache.Builder(this).build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(this.cacheDir.resolve("geography_quiz_image_cache"))
                .maxSizePercent(0.10)
                .build()
        }
        .respectCacheHeaders(enable = false) // TODO: maybe i don't need this. Check further.
        .crossfade(enable = true)
        .logger(DebugLogger())
        .build()
}