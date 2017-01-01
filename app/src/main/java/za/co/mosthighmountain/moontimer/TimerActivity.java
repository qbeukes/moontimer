package za.co.mosthighmountain.moontimer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import za.co.mosthighmountain.moontimer.heartbeat.HeartRateMonitorActivity;


public class TimerActivity extends ActionBarActivity {
    private static int RESULT_SETTINGS = 1;

    private static int RESULT_HEARTBEAT = 2;

    /**
     * Timer task
     */
    private static TimerTask timerTask;

    /**
     * Bell Player
     */
    private static BellPlayer bellPlayer;

    /**
     * List of menu items used for selecting a time mode
     */
    private List<MenuItem> timeModeItems = new ArrayList<MenuItem>();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // genesis: first call of onCreate
        if (bellPlayer == null) {
            bellPlayer = new BellPlayer(this);
            timerTask = new TimerTask(bellPlayer);
            TimerConfig.loadConfig(this);
        }
    }

    @Override
    protected synchronized void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        timerTask.updateView((TextView)findViewById(R.id.timer_text));

        // update the button display objects to match the program state
        if (timerTask.isRunning()) {
            Button button = (Button)findViewById(R.id.start_stop_button);
            button.setText(getString(R.string.action_stop));
        }

        scaleButtons();
    }

    private void scaleButtons() {

    }

    public void startStopTimer(View view) {
        Button button = (Button)view;
        String startString = getString(R.string.action_start);
        String stopString = getString(R.string.action_stop);

        if (startString.equals(button.getText())) {
            timerTask.startTimer();
            button.setText(stopString);
            MoonPhase.refreshMoonLength();
        }
        else if (stopString.equals(button.getText())) {
            timerTask.stopTimer();
            button.setText(startString);
        }

    }

    public void testBell(View view) {
        bellPlayer.playBell(2);
    }

    public void resetTimer(View view) {
        timerTask.resetTimer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timer, menu);

        // find and capture all the TimeMode menu items
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem mi = menu.getItem(i);

            if (TimeMode.getByAndroidId(mi.getItemId()) != null) {
                timeModeItems.add(mi);
            }
        }

        TimeMode savedTimeMode = TimerConfig.getTimeMode();
        updateTimeMode(savedTimeMode);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            showSettingsActivity(null);
            return true;
        }
        else if (id == R.id.action_heartbeat) {
            showHeartbeatActivity(null);
            return true;
        }
        else {
            // check if a time mode option was selected
            for (TimeMode timeMode : TimeMode.values()) {
                if (timeMode.androidId == id) {
                    updateTimeMode(timeMode);
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Complete update the time mode object in all the necessary places
     */
    private void updateTimeMode(TimeMode timeMode) {
        TimerConfig.updateTimeMode(timeMode);
        TimerConfig.saveConfig(TimerActivity.this);
        timerTask.setTimeMode(timeMode);

        getSupportActionBar().setTitle(timeMode.getModeDescription());

        for (MenuItem mi : timeModeItems) {
            boolean selectedItem = (mi.getItemId() == timeMode.androidId);
            mi.setChecked(selectedItem);
        }
    }

    public void showSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, RESULT_SETTINGS);
    }

    public void showHeartbeatActivity(View view) {
        Intent intent = new Intent(this, HeartRateMonitorActivity.class);
        startActivityForResult(intent, RESULT_HEARTBEAT);
    }
}
