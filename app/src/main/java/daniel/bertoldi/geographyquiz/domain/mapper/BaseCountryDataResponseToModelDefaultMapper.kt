package daniel.bertoldi.geographyquiz.domain.mapper

import daniel.bertoldi.geographyquiz.domain.model.CountryCallingCodesModel
import daniel.bertoldi.geographyquiz.domain.model.CountryCarInfoModel
import daniel.bertoldi.geographyquiz.domain.model.CountryModel
import daniel.bertoldi.geographyquiz.domain.model.CountryNameModel
import daniel.bertoldi.geographyquiz.domain.model.DayOfWeek
import daniel.bertoldi.network.BaseCountryDataResponse
import daniel.bertoldi.network.CarResponse
import daniel.bertoldi.network.InternationalDialResponse
import daniel.bertoldi.network.NameDataResponse
import javax.inject.Inject

private const val SMALL_PNG_SIZE = "w320"
private const val LARGE_PNG_SIZE = "w640"

class BaseCountryDataResponseToModelDefaultMapper @Inject constructor() :
    BaseCountryDataResponseToModelMapper {

    override fun mapFrom(from: List<BaseCountryDataResponse>?): List<CountryModel>? {
        return from?.map {
            CountryModel(
                countryCode = it.countryCode,
                name = mapCountryNameModel(it.name),
                topLevelDomains = it.topLevelDomains,
                independent = it.independent,
                unMember = it.unMember,
                internationalDialResponse = mapCountryCallingCodes(it.idd),
                capital = it.capital,
                altSpellings = it.alternativeSpellings,
                region = it.region,
                subRegion = it.subRegion,
                languages = it.languages,
                landlocked = it.landlocked,
                area = it.area,
                emojiFlag = it.emojiFlag,
                population = it.population,
                carInfo = mapCountryCarInfo(it.car),
                timezones = it.timezones,
                continents = it.continents,
                flagPng = mapPngFlag(it.flags.png),
                coatOfArms = it.coatOfArms.png,
                startOfWeek = DayOfWeek.parse(it.startOfWeek),
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

    private fun mapCountryCarInfo(car: CarResponse) = CountryCarInfoModel(
        signs = car.signs,
        side = car.side,
    )

    private fun mapPngFlag(png: String) = png.replace(SMALL_PNG_SIZE, LARGE_PNG_SIZE)
}