package daniel.bertoldi.geographyquiz.domain.mapper

import daniel.bertoldi.database.entities.CarRegulations
import daniel.bertoldi.database.entities.CountryEntity
import daniel.bertoldi.database.entities.CountryNames
import daniel.bertoldi.database.entities.InternationalDialInfo
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import javax.inject.Inject

class CountryModelToEntityDefaultMapper @Inject constructor() : CountryModelToEntityMapper {
    override fun mapFrom(countries: List<CountryModel>): List<CountryEntity> {
        return countries.map {
            CountryEntity(
                countryCode = it.countryCode,
                name = CountryNames(
                    common = it.name.common,
                    official = it.name.official,
                ),
                tld = it.topLevelDomains,
                independent = it.independent,
                unMember = it.unMember,
                idd = InternationalDialInfo(
                    root = it.internationalDialResponse.root,
                    suffixes = it.internationalDialResponse.suffixes,
                ),
                capital = it.capital,
                altSpellings = it.altSpellings,
                region = it.region,
                subRegion = it.subRegion.normalizeSubRegion(),
                languages = it.languages,
                landlocked = it.landlocked,
                area = it.area,
                emojiFlag = it.emojiFlag,
                population = it.population,
                carRegulations = CarRegulations(
                    carSigns = it.carInfo.signs,
                    carSide = it.carInfo.side,
                ),
                timezones = it.timezones,
                continents = it.continents,
                flagPng = it.flagPng,
                coatOfArms = it.coatOfArms,
                startOfWeek = it.startOfWeek.name,
            )
        }
    }

    private fun String?.normalizeSubRegion() = when {
        this?.contains("South-Eastern", ignoreCase = true) == true -> "Southeast"
        else -> this
    }
}