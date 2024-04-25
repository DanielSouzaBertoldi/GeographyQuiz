package daniel.bertoldi.geographyquiz.domain.mapper

import daniel.bertoldi.database.CountryEntity
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import daniel.bertoldi.network.InternationalDialResponse
import daniel.bertoldi.network.NameDataResponse
import javax.inject.Inject

class CountryModelToEntityDefaultMapper @Inject constructor() : CountryModelToEntityMapper {
    override fun mapFrom(countries: List<CountryModel>): List<CountryEntity> {
        return countries.map {
            CountryEntity(
                countryCode = it.countryCode,
                name = NameDataResponse(
                    common = it.name.common,
                    official = it.name.official,
                ),
                tld = it.topLevelDomains,
                independent = it.independent,
                unMember = it.unMember,
                idd = InternationalDialResponse(
                    root = it.internationalDialResponse.root,
                    suffixes = it.internationalDialResponse.suffixes,
                ),
                capital = it.capital,
                altSpellings = it.altSpellings,
                region = it.region,
                subRegion = it.subRegion,
                languages = it.languages,
                landlocked = it.landlocked,
                area = it.area,
                emojiFlag = it.emojiFlag,
                population = it.population,
                carSide = it.carInfo.side,
                carSigns = it.carInfo.signs,
                timezones = it.timezones,
                continents = it.continents,
                flagPng = it.flagPng,
                coatOfArms = it.coatOfArms,
                startOfWeek = it.startOfWeek.name,
            )
        }
    }
}