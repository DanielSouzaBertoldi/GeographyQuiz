package daniel.bertoldi.database

import daniel.bertoldi.network.BaseCountryDataResponse
import javax.inject.Inject

class DatabaseStuff @Inject constructor(
    private val countriesDao: CountriesDao,
) : DatabaseInterface {

    override fun getDb() = countriesDao.getAll()

    // TODO: Maybe I don't need this insert at all.
    override fun saveCountry(country: BaseCountryDataResponse) {
        countriesDao.insertCountry(
            country = CountryEntity(
                countryCode = country.countryCode,
                tld = country.topLevelDomains,
                independent = country.independent ?: false,
                unMember = country.unMember,
                idd = country.idd,
                capital = country.capital,
                altSpellings = country.alternativeSpellings,
                region = country.region,
                subRegion = country.subRegion,
                languages = country.languages,
                landlocked = country.landlocked,
                area = country.area,
                emojiFlag = country.emojiFlag,
                population = country.population,
                carSigns = country.car.signs,
                carSide = country.car.side, // TODO: this could be an enum. Left/Right.
                timezones = country.timezones,
                continents = country.continents,
                flagPng = country.flags.png,
                coatOfArms = country.coatOfArms.png,
                startOfWeek = country.startOfWeek,
            )
        )
    }

    override fun saveCountries(countries: List<BaseCountryDataResponse>) {
        countriesDao.insertCountries(
            countries.map {
                CountryEntity(
                    countryCode = it.countryCode,
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
}