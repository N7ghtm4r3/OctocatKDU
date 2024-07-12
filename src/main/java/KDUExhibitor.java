import params.FrequencyVisibility;

import java.util.prefs.Preferences;

/**
 * The {@code KDUExhibitor} class is useful to check whether the dialog can be displayed or not according to the user
 * and developer settings
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @since 1.0.3
 */
//TODO: TO COMMENT
class KDUExhibitor {

    /**
     * {@code preferences} the preferences manager with the details saved by the user
     */
    private static final Preferences preferences = Preferences.userRoot().node("user/tecknobit/octocatkdu");

    /**
     * {@code NOT_SHOW_AT_NEXT_LAUNCH_KEY} the key for the not show at next launch option
     */
    private static final String NOT_SHOW_AT_NEXT_LAUNCH_KEY = "not_show_at_next_launch";

    /**
     * {@code LAST_TIME_DIALOG_DISPLAYED_KEY} the key for the last time dialog displayed value
     */
    private static final String LAST_TIME_DIALOG_DISPLAYED_KEY = "last_time_dialog_displayed";

    private final FrequencyVisibility frequencyVisibility;

    public KDUExhibitor() {
        this(FrequencyVisibility.ALWAYS);
    }

    public KDUExhibitor(FrequencyVisibility frequencyVisibility) {
        this.frequencyVisibility = frequencyVisibility;
    }

    /**
     * Method to set the option whether display the dialog at the next launches
     *
     * @param notShowAtNextLaunch: flag to set to display or not the dialog
     */
    public void notShowAtNextLaunch(boolean notShowAtNextLaunch) {
        preferences.putBoolean(NOT_SHOW_AT_NEXT_LAUNCH_KEY, !notShowAtNextLaunch);
    }

    /**
     * Method to get whether the dialog can be displayed or not <br>
     *
     * @param isFakeDialog: whether the dialog that is testing is the real one or the fake
     *
     * @return whether the dialog can be displayed or not as boolean
     */
    public boolean canDisplayDialog(boolean isFakeDialog) {
        if(isFakeDialog)
            return true;
        return canBeShownAtTheNextLaunch() && isFrequencyVisibilityExceeded();
    }

    private boolean canBeShownAtTheNextLaunch() {
        return preferences.getBoolean(NOT_SHOW_AT_NEXT_LAUNCH_KEY, true);
    }

    private boolean isFrequencyVisibilityExceeded() {
        long lastDisplayedTime = getLastDisplayedTime();
        return System.currentTimeMillis() - lastDisplayedTime >= frequencyVisibility.getGap();
    }

    /**
     * Method to refresh the last time when the dialog has been shown <br>
     * No-any params required
     *
     */
    public void refreshDisplayedTime() {
        preferences.putLong(LAST_TIME_DIALOG_DISPLAYED_KEY, System.currentTimeMillis());
    }

    /**
     * Method to get the last time when the dialog has been shown <br>
     * No-any params required
     *
     * @return the last time when the dialog has been shown as long
     */
    public long getLastDisplayedTime() {
        return preferences.getLong(LAST_TIME_DIALOG_DISPLAYED_KEY, System.currentTimeMillis());
    }

}
