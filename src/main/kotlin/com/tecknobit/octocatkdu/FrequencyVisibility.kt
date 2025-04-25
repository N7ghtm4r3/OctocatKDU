package com.tecknobit.octocatkdu

import java.util.concurrent.TimeUnit.DAYS

/**
 * The temporal frequency when display the dialog when there is an update available
 *
 * @param gap The gap value used to check if the dialog can be displayed due to exceed of
 * the frequency visibility chosen
 *
 * @since 1.0.4
 */
enum class FrequencyVisibility(
    val gap: Long
) {

    /**
     * `ALWAYS` the dialog will always be displayed at every launch
     */
    ALWAYS(
        gap = 0
    ),

    /**
     * `ONCE_PER_DAY` the dialog will be displayed only once per day
     */
    ONCE_PER_DAY(
        gap = DAYS.toMillis(1)
    ),

    /**
     * `ONCE_PER_WEEK` the dialog will be displayed only once per week
     */
    ONCE_PER_WEEK(
        gap = DAYS.toMillis(7)
    ),

    /**
     * `ONCE_PER_MONTH` the dialog will be displayed only once per month (considering 30 days value)
     */
    ONCE_PER_MONTH(
        gap = DAYS.toMillis(30)
    )

}