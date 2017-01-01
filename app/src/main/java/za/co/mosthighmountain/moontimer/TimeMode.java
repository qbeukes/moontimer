package za.co.mosthighmountain.moontimer;

/**
 * Created by quintin on 2016/09/11.
 */
public enum TimeMode {
    /**
     * The mode of time is the same as the operating system's time, and based off synchronized
     * to atomic clocks.
     */
    NORMAL(R.id.action_normaltime) {
        public long translateMilliseconds(long ostimeElapsed) {
            return ostimeElapsed;
        }

        public String getModeDescription() {
            return "Normal Time";
        }
    },

    /**
     * This mode of time is based of the duration of the moon in which the timer was started.
     * If the moon is 29.5days, then the seconds will be adjusted to 29.5/30 days
     */
    MOON(R.id.action_moontime) {
        public double getTimeWarpFactor() {
            return MoonPhase.getCurrentMoonLength() / (30.0f * 24 * 3600);
        }

        public long translateMilliseconds(long ostimeElapsed) {
            return (long)(ostimeElapsed * getTimeWarpFactor());
        }

        public String getModeDescription() {
            return String.format("Moon Time (%.05f)", getTimeWarpFactor());
        }
    };

    /**
     * Calculate the number of elapsed seconds given a number of elapsed seconds from the
     * operating system time
     * @param ostimeElapsed Milliseconds elapsed
     */
    public abstract long translateMilliseconds(long ostimeElapsed);

    /**
     * Returns a friend description of this time mode
     */
    public abstract String getModeDescription();

    public int androidId;

    TimeMode(int androidId) {
        this.androidId = androidId;
    }

    /**
     * Will look for a TimeMode that has the given android ID
     * @param androidId
     * @return TimeMode or null if not found
     */
    public static TimeMode getByAndroidId(int androidId) {
        for (TimeMode timeMode : values()) {
            if (timeMode.androidId == androidId) {
                return timeMode;
            }
        }

        return null;
    }
}
