package daniel.bertoldi.database

import daniel.bertoldi.network.BaseCountryDataResponse
import javax.inject.Inject

class DatabaseStuff @Inject constructor(
    private val countriesDao: CountriesDao,
) : DatabaseInterface {

    override fun getAllCountries() = countriesDao.getAll()

    override fun fetchCountriesCount() = countriesDao.fetchCountriesCount()

    override fun saveCountries(countries: List<BaseCountryDataResponse>) {
        countriesDao.insertCountries(
            countries.map {
                CountryEntity(
                    countryCode = it.countryCode,
                    name = it.name,
                    tld = it.topLevelDomains,
                    independent = it.independent ?: false,
                    unMember = it.unMember,
                    idd = it.idd,
                    capital = it.capital,
                    altSpellings = it.alternativeSpellings,
                    region = it.region,
                    subRegion = it.subRegion,
                    languages = it.languages,
                    landlocked = it.landlocked,
                    area = it.area,
                    emojiFlag = it.emojiFlag,
                    population = it.population,
                    carSigns = it.car.signs,
                    carSide = it.car.side, // TODO: this could be an enum. Left/Right.
                    timezones = it.timezones,
                    continents = it.continents,
                    flagPng = it.flags.png,
                    coatOfArms = it.coatOfArms.png,
                    startOfWeek = it.startOfWeek,
                )
            }
        )
    }

    override fun fetchCountriesInContinent(continent: String) =
        countriesDao.fetchCountriesInContinent(continent)
}