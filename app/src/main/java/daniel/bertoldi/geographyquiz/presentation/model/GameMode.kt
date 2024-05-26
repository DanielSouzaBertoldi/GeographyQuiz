package daniel.bertoldi.geographyquiz.presentation.model

import daniel.bertoldi.geographyquiz.R

private const val CASUAL = "Casual"
private const val TIME_ATTACK = "Time Attack"
private const val SUDDEN_DEATH = "Sudden Death"

sealed class GameMode {
    abstract val name: String
    abstract val icon: Int
    abstract val description: Int
    abstract val title: Int

    data class Casual(
        override val name: String = CASUAL,
        override val icon: Int = R.drawable.casual,
        override val description: Int = R.string.game_mode_casual_description,
        override val title: Int = R.string.game_mode_casual,
    ) : GameMode()

    data class TimeAttack(
        override val name: String = TIME_ATTACK,
        override val icon: Int = R.drawable.time_attack,
        override val description: Int = R.string.game_mode_time_attack_description,
        override val title: Int = R.string.game_mode_time_attack,
    ) : GameMode()

    data class SuddenDeath(
        override val name: String = SUDDEN_DEATH,
        override val icon: Int = R.drawable.sudden_death,
        override val description: Int = R.string.game_mode_sudden_death_description,
        override val title: Int = R.string.game_mode_sudden_death,
    ) : GameMode()

    companion object {
        fun String.toGameMode() = when {
            this.equals(CASUAL, ignoreCase = true) -> Casual()
            this.equals(TIME_ATTACK, ignoreCase = true) -> TimeAttack()
            else -> SuddenDeath()
        }
    }
}
