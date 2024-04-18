package daniel.bertoldi.geographyquiz.domain.model

enum class DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY,
    UNKNOWN;

    companion object {
        fun parse(day: String) = entries.find {
            it.name.equals(day, ignoreCase = true)
        } ?: UNKNOWN
    }
}