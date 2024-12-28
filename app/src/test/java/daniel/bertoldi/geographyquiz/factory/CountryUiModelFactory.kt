package daniel.bertoldi.geographyquiz.factory

import daniel.bertoldi.geographyquiz.presentation.model.CountryFlagUi
import daniel.bertoldi.utilities.test.utils.randomString
import daniel.bertoldi.utilities.test.utils.randomUrl

object CountryUiModelFactory {

    fun make(
        countryCode: String = randomString(),
        countryName: String = randomString(),
        flagUrl: String = randomUrl(),
    ) = CountryFlagUi(
        countryCode = countryCode,
        countryName = countryName,
        flagUrl = flagUrl,
    )

    fun makeList(
        listSize: Int = 5,
    ): List<CountryFlagUi> = mutableListOf<CountryFlagUi>().apply {
        repeat(listSize) {
            add(make())
        }
    }.toList()
}