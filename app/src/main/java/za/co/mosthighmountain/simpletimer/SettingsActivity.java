package za.co.mosthighmountain.simpletimer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class SettingsActivity extends ActionBarActivity {

    private static final int TIME_SPINNER_MIN = 1;

    private static final int TIME_SPINNER_MAX = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        configureTimeSpinner(1, R.id.time_spinner_1);
        configureTimeSpinner(2, R.id.time_spinner_2);
        configureTimeSpinner(3, R.id.time_spinner_3);
    }

    private void configureTimeSpinner(int bellNo, int spinnerViewId) {
        Spinner spinner = (Spinner)findViewById(spinnerViewId);

        spinner.setAdapter(new TimeSpinnerAdapter());

//        TextView oTextView;
//        oTextView = (TextView)spinner.getChildAt(0);
//        oTextView.setTextColor(Color.RED);

        setSpinnerValueFromConfig(bellNo, spinner);
        configureConfigUpdate(bellNo, spinner);
    }

    private void configureConfigUpdate(final int bellNo, Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TimerConfig.updateBellTime(bellNo, position);
                TimerConfig.saveConfig(SettingsActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinnerValueFromConfig(int bellNo, Spinner spinner) {
        int bellTime = TimerConfig.getBellTime(bellNo);
        spinner.setSelection(bellTime);
    }

    public void resetTimes(View view) {
        TimerConfig.restoreDefaultConfig(this);

        configureTimeSpinner(1, R.id.time_spinner_1);
        configureTimeSpinner(2, R.id.time_spinner_2);
        configureTimeSpinner(3, R.id.time_spinner_3);
    }

    private class TimeSpinnerAdapter extends ArrayAdapter<CharSequence> {
        public TimeSpinnerAdapter() {
            super(SettingsActivity.this, R.layout.time_spinner_style);

            add("No Bell");
            for (int i = TIME_SPINNER_MIN; i <= TIME_SPINNER_MAX; ++i) {
                add(Integer.toString(i));
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView label = new TextView(SettingsActivity.this);
            label.setTextColor(Color.GRAY);
            label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27);

            if (position == 0) {
                label.setText("No Bell");
            }
            else {
                label.setText(position + " minutes");
            }

            return label;
        }
    }
}
