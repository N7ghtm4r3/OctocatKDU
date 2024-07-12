package params

import params.FrequencyVisibility.ALWAYS
import java.util.*
import java.util.concurrent.TimeUnit.DAYS

enum class FrequencyVisibility(
    val gap: Long
) {

    ALWAYS(
        gap = 0
    ),

    ONCE_PER_DAY(
        gap = DAYS.toMillis(1)
    ),

    ONCE_PER_WEEK(
        gap = DAYS.toMillis(7)
    ),

    ONCE_PER_MONTH(
        gap = DAYS.toMillis(30)
    )

}

data class OctocatKDUConfig(
    val frequencyVisibility: FrequencyVisibility = ALWAYS,
    val locale: Locale = Locale.getDefault(),
    val notShowAtNextLaunchOptionEnabled: Boolean = false
)