package daniel.bertoldi.database.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import daniel.bertoldi.database.dao.CountriesDao
import daniel.bertoldi.database.typeconverters.InternationalDialInfoTypeConverter
import daniel.bertoldi.database.GeographyQuizDatabase
import daniel.bertoldi.database.dao.HighScoresDao
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
    ): GeographyQuizDatabase {
        return Room.databaseBuilder(
            context = appContext,
            klass = GeographyQuizDatabase::class.java,
            name = DATABASE_NAME,
        ).addTypeConverter(InternationalDialInfoTypeConverter(moshi)).build()
    }

    @Provides
    fun provideCountriesDao(appDatabase: GeographyQuizDatabase): CountriesDao {
        return appDatabase.countriesDao()
    }

    @Provides
    fun provideHighScoresDao(appDatabase: GeographyQuizDatabase): HighScoresDao {
        return appDatabase.highScoredDao()
    }
}