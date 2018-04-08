package android.connectify.com.connectify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yakov on 4/1/2018.
 */

public class SettingsHelper {
    private SQLiteOpenHelper _openHelper;

    String TABLE_NAME = "settings";
    String COL1 = "_ID";
    String COL2 = "name";
    String COL3 = "value";

    public SettingsHelper(Context context) {
        _openHelper = new SimpleSQLiteOpenHelper(context);
    }

    class SimpleSQLiteOpenHelper extends SQLiteOpenHelper {
        SimpleSQLiteOpenHelper(Context context) {
            super(context, "main.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COL1 +" integer primary key autoincrement, " + COL2 + " text unique, " + COL3 + " text)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public Cursor getAll() {
        SQLiteDatabase db = _openHelper.getReadableDatabase();
        if (db == null) {
            return null;
        }
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public String getValueByName(String name) {
        SQLiteDatabase db = _openHelper.getReadableDatabase();
        if (db == null) {
            return "";
        }

        String value = "";

        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL2 + " = ? LIMIT 1", new String[] { name });
        if (cur.moveToNext()) {
            value = cur.getString(2);
        }

        cur.close();
        db.close();
        return value;
    }

    public void add(String name, String value) {
        SQLiteDatabase db = _openHelper.getWritableDatabase();

        if (db != null) {
            ContentValues row = new ContentValues();
            row.put(COL2, name);
            row.put(COL3, value);
            db.insert(TABLE_NAME, null, row);
            db.close();
        }
    }

    public void delete(String name) {
        SQLiteDatabase db = _openHelper.getWritableDatabase();
        if (db == null) {
            return;
        }
        db.delete(TABLE_NAME, COL2 + " = ?", new String[] { name });
        db.close();
    }

    public boolean is_initialized () {
        return (this.getValueByName("device_hash") != "");
    }

    public void initialize (String device_hash, String device_name) {
        this.add("initialized", "1");
        this.add("device_name", device_hash);
        this.add("device_hash", device_hash);
    }

    public void uninitialize () {
        this.delete("initialized");
        this.delete("device_name");
        this.delete("device_hash");
    }

    private String getTimestamp () {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.US).format(new Date());
    }
}
