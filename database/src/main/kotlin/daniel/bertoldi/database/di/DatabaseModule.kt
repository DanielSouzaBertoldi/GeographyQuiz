package daniel.bertoldi.database.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import daniel.bertoldi.database.DatabaseInterface
import daniel.bertoldi.database.DatabaseStuff
import daniel.bertoldi.database.TestDao
import daniel.bertoldi.database.TestDatabase
import javax.inject.Singleton

private const val DATABASE_NAME = "GeographyQuizDatabase"

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): TestDatabase {
        return Room.databaseBuilder(
            appContext,
            TestDatabase::class.java,
            DATABASE_NAME,
        ).build()
    }

    @Provides
    fun provideTestDao(appDatabase: TestDatabase): TestDao {
        return appDatabase.testDao()
    }

    @Provides
    fun provideDatabaseInterface(databaseStuff: DatabaseStuff): DatabaseInterface {
        return databaseStuff
    }

//    This @Binds could be used to replace the @Provides above, however class should only
//    be abstract or an interface.
//    https://github.com/google/dagger/issues/1691
//    https://stackoverflow.com/questions/52586940/what-is-the-use-case-for-binds-vs-provides-annotation-in-dagger2
//    @Binds
//    abstract fun bindDatabaseInterface(databaseStuff: DatabaseStuff): DatabaseInterface
}