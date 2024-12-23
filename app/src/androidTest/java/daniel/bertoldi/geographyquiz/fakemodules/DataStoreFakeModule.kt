package daniel.bertoldi.geographyquiz.fakemodules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import daniel.bertoldi.geographyquiz.di.DataStoreModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton
import kotlin.random.Random
import kotlin.random.nextUInt

internal const val FAKE_DATA_STORE_FILE_PREFIX = "fake_data_store-"

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class],
)
class DataStoreFakeModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        val randInt = Random.nextUInt()
        return PreferenceDataStoreFactory.create(
            // TODO: idk why but using TestScope() completely messes up instrumented test...
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = {
                appContext.preferencesDataStoreFile("$FAKE_DATA_STORE_FILE_PREFIX-$randInt")
            }
        )
    }
}
