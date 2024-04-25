package daniel.bertoldi.geographyquiz.datastore

interface CountriesDataStore {

    suspend fun checkCacheGreaterThanSevenDays(): Boolean // TODO: this name is kinda trash, think of a better name for it.
    suspend fun saveFetchTime(currentTime: Long)
}