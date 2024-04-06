package daniel.bertoldi.database

import javax.inject.Inject

class DatabaseStuff @Inject constructor(
    private val testDao: TestDao,
) : DatabaseInterface {

    override fun getDb() = testDao.getAll()
    override fun saveToDb(flags: List<String>) { TODO("Not yet implemented") }
}