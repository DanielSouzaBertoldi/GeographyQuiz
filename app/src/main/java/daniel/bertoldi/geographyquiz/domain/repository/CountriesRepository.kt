package daniel.bertoldi.geographyquiz.domain.repository

interface CountriesRepository {
    suspend fun getCountries()
}