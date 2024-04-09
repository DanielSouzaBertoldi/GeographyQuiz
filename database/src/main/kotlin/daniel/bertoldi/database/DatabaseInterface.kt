package daniel.bertoldi.database

interface DatabaseInterface {

    fun getDb(): List<Test>

    fun saveToDb(flags: List<String>) // TODO: Change this to the model in the
}