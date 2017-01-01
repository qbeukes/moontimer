package za.co.mosthighmountain.simpletimer;

import android.content.Context;

/**
 * Created by Quintin on 8/23/2015.
 */
public class TimerConfig {
    /**
     * Number of minutes for each bell
     */
    private static int[] bells = new int[3];

    /**
     * Mode of time
     */
    private static TimeMode timeMode;

    /**
     * @param bellNo
     * @return The number of minutes at which to play the specified bell
     */
    public static int getBellTime(int bellNo) {
        if (bellNo < 1 || bellNo > 3) {
            throw new IllegalArgumentException("Invalid bell number: " + bellNo);
        }

        return bells[bellNo - 1];
    }

    /**
     * Set the number of minutes at which to play the specified bell
     * @param bellNo
     * @param minutes
     */
    public static void updateBellTime(int bellNo, int minutes) {
        if (bellNo < 1 || bellNo > 3) {
            throw new IllegalArgumentException("Invalid bell number: " + bellNo);
        }

        bells[bellNo - 1] = minutes;
    }

    /**
     * Get the last used TimeMode object
     */
    public static TimeMode getTimeMode() {
        return timeMode;
    }

    /**
     * Save a new time mode
     */
    public static void updateTimeMode(TimeMode timeMode) {
        TimerConfig.timeMode = timeMode;
    }

    /**
     * Load the bell configurations from shared preferences
     * @param context
     */
    public static void loadConfig(Context context) {
        TimerDb db = new TimerDb(context);
        bells[0] = db.loadBellTime(1, 1);
        bells[1] = db.loadBellTime(2, 0);
        bells[2] = db.loadBellTime(3, 0);
        timeMode = db.loadTimeMode("MOON");
    }

    /**
     * Save the bell configurations to shared preferences
     * @param context
     */
    public static void saveConfig(Context context) {
        TimerDb db = new TimerDb(context);
        db.saveBellTime(1, bells[0]);
        db.saveBellTime(2, bells[1]);
        db.saveBellTime(3, bells[2]);
        db.saveTimeMode(timeMode);
    }

    /**
     * Remove all SharedPreferences and reload using loadConfig
     * @param context
     */
    public static void restoreDefaultConfig(Context context) {
        TimerDb db = new TimerDb(context);
        db.purgeAllData();

        loadConfig(context);
    }

    /**
     * @return The number of bells we have configured in this profile
     */
    public static int getNumberOfBells() {
        return bells.length;
    }
}
