package daniel.bertoldi.geographyquiz.presentation.mapper

import daniel.bertoldi.geographyquiz.domain.model.HighScoreModel
import daniel.bertoldi.geographyquiz.presentation.model.HighScoresUIModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import javax.inject.Inject

class HighScoresModelToUiDefaultMapper @Inject constructor() : HighScoresModelToUiMapper {
    override suspend fun mapFrom(highScores: List<HighScoreModel>) = highScores.map {
        HighScoresUIModel(
            score = it.score,
            accuracy = it.accuracy,
            hits = it.hits,
            misses = it.misses,
            timeElapsed = it.timeElapsed,
            datePlayed = it.dateEpoch.toFormattedDateTime(),
        )
    }

    private fun Long.toFormattedDateTime(): String {
        val instant = Instant.ofEpochMilli(this)
        val date = instant.atZone(ZoneId.systemDefault()).toLocalDate()
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
            .withLocale(Locale.getDefault())
        return date.format(formatter)
    }
}