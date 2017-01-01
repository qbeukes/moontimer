package za.co.mosthighmountain.simpletimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Quintin on 9/27/2015.
 *
 * This is an encapsulation of a SQLite db to store app config
 */
public class TimerDb extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "timerdb";

    private static final String TABLE_CONFIG = "config";

    private static final String CREATE_CONFIG =
            "CREATE TABLE config (key TEXT, value TEXT)";

    private static final String TABLE_PRESETS = "presets";

    private static final String CREATE_PRESETS =
            "CREATE TABLE presets (preset TEXT, bellno INTEGER, belltime INTEGER)";

    private static final String DEFAULT_PRESET = "default";

    private static final String CONFIG_TYPEOFSECOND = "typeofsecond";

    public TimerDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Get the TimeMode config, with a default if the config doesn't exist
     */
    public TimeMode loadTimeMode(String defaultTos) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT value FROM " + TABLE_CONFIG + " WHERE key=?";
        Cursor cursor = db.rawQuery(sql, new String[] {
                CONFIG_TYPEOFSECOND
        });

        String str = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            str = cursor.getString(0);
        }

        try {
            return TimeMode.valueOf(str);
        }
        catch (Exception e) {
            return TimeMode.valueOf(defaultTos);
        }
    }

    /**
     * Save the TimeMode config
     */
    public void saveTimeMode(TimeMode timeMode) {
        if (timeMode == null) {
            throw new IllegalArgumentException("TimeMode can't be null.");
        }

        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_CONFIG, "key=?", new String[] {
                CONFIG_TYPEOFSECOND
        });

        ContentValues values = new ContentValues();
        values.put("key", CONFIG_TYPEOFSECOND);
        values.put("value", timeMode.name());
        db.insert(TABLE_CONFIG, null, values);
    }

    /**
     * Get the requested bell time. Will return a given default if the row doesn't exist
     *
     * @param bellNo
     * @param defaultTime
     */
    public int loadBellTime(int bellNo, int defaultTime) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT belltime FROM " + TABLE_PRESETS + " WHERE preset=? AND bellno=?";
        Cursor cursor = db.rawQuery(sql, new String[] {
                DEFAULT_PRESET,
           Integer.toString(bellNo)
        });

        if (cursor.getCount() == 0) {
            return defaultTime;
        }
        else {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
    }

    /**
     * Save the specified bell time
     * @param bellNo
     * @param bellTime
     */
    public void saveBellTime(int bellNo, int bellTime) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_PRESETS, "preset=? AND bellno=?", new String[] {
                DEFAULT_PRESET,
           Integer.toString(bellNo)
        });

        ContentValues values = new ContentValues();
        values.put("preset", DEFAULT_PRESET);
        values.put("bellno", bellNo);
        values.put("belltime", bellTime);
        db.insert(TABLE_PRESETS, null, values);
    }

    /**
     * Delete all data from all tables
     */
    public void purgeAllData() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CONFIG, null, null);
        db.delete(TABLE_PRESETS, null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONFIG);
        db.execSQL(CREATE_PRESETS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
