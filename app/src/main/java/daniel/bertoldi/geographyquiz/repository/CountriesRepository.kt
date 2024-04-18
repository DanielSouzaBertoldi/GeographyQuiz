package daniel.bertoldi.geographyquiz.repository

interface CountriesRepository {
    suspend fun getCountries()
}