package daniel.bertoldi.geographyquiz.domain.mapper

import daniel.bertoldi.database.CountryEntity
import daniel.bertoldi.geographyquiz.domain.model.CountryCallingCodesModel
import daniel.bertoldi.geographyquiz.domain.model.CountryCarInfoModel
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import daniel.bertoldi.geographyquiz.domain.model.CountryNameModel
import daniel.bertoldi.geographyquiz.domain.model.DayOfWeek
import daniel.bertoldi.network.InternationalDialResponse
import daniel.bertoldi.network.NameDataResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CountryEntityToModelDefaultMapper @Inject constructor() : CountryEntityToModelMapper {

    override fun mapFrom(from: List<CountryEntity>): List<CountryModel> {
        return from.map {
            CountryModel(
                countryCode = it.countryCode,
                name = mapCountryNameModel(it.name),
                topLevelDomains = it.tld,
                independent = it.independent,
                unMember = it.unMember,
                internationalDialResponse = mapCountryCallingCodes(it.idd),
                capital = it.capital,
                altSpellings = it.altSpellings,
                region = it.region,
                subRegion = it.subRegion,
                languages = it.languages,
                landlocked = it.landlocked,
                area = it.area,
                emojiFlag = it.emojiFlag,
                population = it.population,
                carInfo = mapCountryCarInfoModel(it.carSigns, it.carSide),
                timezones = it.timezones,
                continents = it.continents,
                flagPng = it.flagPng,
                coatOfArms = it.coatOfArms,
                startOfWeek = DayOfWeek.parse(it.startOfWeek)
            )
        }
    }

    private fun mapCountryNameModel(name: NameDataResponse) = CountryNameModel(
        common = name.common,
        official = name.official,
    )

    private fun mapCountryCallingCodes(idd: InternationalDialResponse) = CountryCallingCodesModel(
        root = idd.root,
        suffixes = idd.suffixes,
    )

    private fun mapCountryCarInfoModel(carSigns: List<String>?, carSide: String) =
        CountryCarInfoModel(
            signs = carSigns,
            side = carSide,
        )
}