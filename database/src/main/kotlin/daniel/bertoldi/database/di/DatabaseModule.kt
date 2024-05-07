package daniel.bertoldi.database.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import daniel.bertoldi.database.CountriesDao
import daniel.bertoldi.database.CountriesDatabaseInterface
import daniel.bertoldi.database.CountriesDatabaseStuff
import daniel.bertoldi.database.typeconverters.InternationalDialTypeConverter
import daniel.bertoldi.database.CountriesDatabase
import javax.inject.Singleton

private const val DATABASE_NAME = "GeographyQuizDatabase"

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context,
        moshi: Moshi
    ): CountriesDatabase {
        // val international = InternationalDialTypeConverter(moshi)
        return Room.databaseBuilder(
            appContext,
            CountriesDatabase::class.java,
            DATABASE_NAME,
        ).addTypeConverter(InternationalDialTypeConverter(moshi)).build()
    }

    @Provides
    fun provideCountriesDao(appDatabase: CountriesDatabase): CountriesDao {
        return appDatabase.countriesDao()
    }

    @Provides
    fun provideDatabaseInterface(databaseStuff: CountriesDatabaseStuff): CountriesDatabaseInterface {
        return databaseStuff
    }

//    This @Binds could be used to replace the @Provides above, however class should only
//    be abstract or an interface.
//    https://github.com/google/dagger/issues/1691
//    https://stackoverflow.com/questions/52586940/what-is-the-use-case-for-binds-vs-provides-annotation-in-dagger2
//    @Binds
//    abstract fun bindDatabaseInterface(databaseStuff: DatabaseStuff): DatabaseInterface
}