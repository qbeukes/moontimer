package za.co.mosthighmountain.simpletimer;

import android.os.AsyncTask;
import android.widget.TextView;

/**
 * Created by Quintin on 5/24/2015.
 */
public class TimerTask {
    /**
     * Time the current timer run has start
     */
    private long timeStarted;

    /**
     * Last millisecond value when the timer was paused
     */
    private long timeBaseValue = 0;

    /**
     * This time mode used to calculate the perceived seconds
     */
    private TimeMode timeMode = TimeMode.NORMAL;

    /**
     * Parent text view
     */
    private TextView timerText;

    /**
     * Bell player
     */
    private BellPlayer bellPlayer;

    /**
     * Latest AsyncTask
     */
    private TimerAsyncTask lastAsyncTask;

    /**
     * Basic constructor
     * @param bellPlayer
     */
    public TimerTask(BellPlayer bellPlayer) {
        this.bellPlayer = bellPlayer;
    }

    /**
     * Updates the view the timer is linked to. This often needs to happen when the view is
     * redrawn, like with a screen tilt.
     */
    public void updateView(TextView timerText) {
        this.timerText = timerText;
    }

    /**
     * This method will see if the current timer value requires a bell to be played, and will then
     * do so
     */
    private void playBells() {
        long elapsedSeconds = getElapsedPerceivedSeconds();

        // for 0 seconds don't scan configs for bells because "No Bell" is a 0 value
        if (elapsedSeconds == 0) {
            return;
        }

        int numBells = TimerConfig.getNumberOfBells();

        // we iterate from the higher number down to 1, so that if two bells have the same
        // configuration, we only play the bells for the higher one
        for (int bellNo = numBells; bellNo >= 1; --bellNo) {
            int bellSeconds = TimerConfig.getBellTime(bellNo) * 60;

            if (bellSeconds == elapsedSeconds) {
                bellPlayer.playBell(bellNo);
                break;
            }
        }
    }

    private long getElapsedPerceivedSeconds() {
        return timeMode.translateMilliseconds(getElapsedMilliseconds()) / 1000;
    }

    private long getElapsedMilliseconds() {
        return (System.currentTimeMillis() - timeStarted) + timeBaseValue;
    }

    private String calculateTimerValue() {
        long elapsedSeconds = getElapsedPerceivedSeconds();
        long elapsedMinutes = (elapsedSeconds - (elapsedSeconds % 60)) / 60;
        elapsedSeconds = elapsedSeconds % 60;

        StringBuilder sb = new StringBuilder();

        if (elapsedMinutes < 10) {
            sb.append("0").append(elapsedMinutes);
        }
        else {
            sb.append(elapsedMinutes);
        }

        sb.append(":");

        if (elapsedSeconds < 10) {
            sb.append("0").append(elapsedSeconds);
        }
        else {
            sb.append(elapsedSeconds);
        }

        return sb.toString();
    }

    public void startTimer() {
        timeStarted = System.currentTimeMillis();

        if (lastAsyncTask != null) {
            lastAsyncTask.cancel(true);
        }

        lastAsyncTask = new TimerAsyncTask();
        lastAsyncTask.execute();
    }

    public void stopTimer() {
        timeBaseValue = getElapsedMilliseconds();

        if (lastAsyncTask != null) {
            lastAsyncTask.cancel(true);
            lastAsyncTask = null;
        }
    }

    public boolean isRunning() {
        return lastAsyncTask != null;
    }

    public void resetTimer() {
        timeStarted = System.currentTimeMillis();
        timeBaseValue = 0;

        setTimerText("00:00");
    }

    private void setTimerText(String text) {
        if (timerText == null) {
            // you somehow missed running updateView(...)
            throw new RuntimeException("TimerTask view not updated or invalid.");
        }

        timerText.setText(text);
    }

    /**
     * Update the TimeMode object used to calculate perceived seconds. Note that this will
     * reset the timer if the time mode is different.
     */
    public void setTimeMode(TimeMode timeMode) {
        if (timeMode == null) {
            throw new IllegalArgumentException("Invaid time mode supplied.");
        }

        if (this.timeMode != timeMode) {
            this.timeMode = timeMode;
            resetTimer();
        }
    }

    /**
     * The actual execution task
     */
    private class TimerAsyncTask extends AsyncTask<Void, String, Void> {
        @Override
        protected void onProgressUpdate(String... values) {
            setTimerText(values[0]);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                bellPlayer.playBell(1);

                while (!isCancelled()) {
                    String value = calculateTimerValue();
                    playBells();
                    publishProgress(value);

                    Thread.sleep(50);
                }
            }
            catch (InterruptedException e) {}

            return null;
        }
    }
}
