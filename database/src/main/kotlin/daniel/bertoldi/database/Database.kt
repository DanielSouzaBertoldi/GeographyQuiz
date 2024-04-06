package daniel.bertoldi.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Test::class], version = 1)
abstract class TestDatabase : RoomDatabase() {
    abstract fun testDao(): TestDao
}