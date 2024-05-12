package daniel.bertoldi.geographyquiz.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import daniel.bertoldi.geographyquiz.R

sealed class GameMode {
    @get:DrawableRes
    abstract val icon: Int

    @get:StringRes
    abstract val description: Int

    @get:StringRes
    abstract val name: Int

    data class Casual(
        override val icon: Int = R.drawable.casual,
        override val description: Int = R.string.game_mode_casual_description,
        override val name: Int = R.string.game_mode_casual,
    ) : GameMode()

    data class TimeAttack(
        override val icon: Int = R.drawable.time_attack,
        override val description: Int = R.string.game_mode_time_attack_description,
        override val name: Int = R.string.game_mode_time_attack,
    ) : GameMode()

    data class SuddenDeath(
        override val icon: Int = R.drawable.sudden_death,
        override val description: Int = R.string.game_mode_sudden_death_description,
        override val name: Int = R.string.game_mode_sudden_death,
    ) : GameMode()
}
