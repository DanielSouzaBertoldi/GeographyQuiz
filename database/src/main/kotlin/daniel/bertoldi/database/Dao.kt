package daniel.bertoldi.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TestDao {
    @Query("SELECT * FROM test")
    fun getAll(): List<Test>

    @Insert
    fun insertTest(test: Test)

    @Delete
    fun deleteTest(test: Test)
}