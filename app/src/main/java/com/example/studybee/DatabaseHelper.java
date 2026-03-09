package com.example.studybee;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "studybee.db";
    private static final int DATABASE_VERSION = 1;

    // Session table
    public static final String TABLE_SESSIONS = "sessions";
    public static final String COL_ID = "id";
    public static final String COL_START = "start_time";
    public static final String COL_END = "end_time";
    public static final String COL_DURATION = "duration";
    public static final String COL_DATE = "date";

    // User table
    public static final String TABLE_USER = "User";
    public static final String COL_USERNAME = "username";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_SESSIONS = "CREATE TABLE " + TABLE_SESSIONS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_START + " VARCHAR(50), " +
                COL_END + " VARCHAR(50), " +
                COL_DURATION + " LONG, " +
                COL_DATE + " TEXT)";

        db.execSQL(CREATE_TABLE_SESSIONS);

        String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_USER +
                " (" + COL_USERNAME + " VARCHAR(50))";

        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    // ----------- USER METHODS -----------

    // Vstavi ali posodobi username (če želiš samo enega uporabnika)
    public boolean insertOrUpdateUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("CREATE TABLE IF NOT EXISTS User(username VARCHAR(50));");

        // Preveri, če je že username
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER, null);
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);

        boolean success;
        if(cursor.moveToFirst()){
            // update
            success = db.update(TABLE_USER, values, null, null) != -1;
        } else {
            // insert
            success = db.insert(TABLE_USER, null, values) != -1;
        }

        cursor.close();
        return success;
    }

    // Vrne username ali null, če ni
    public String getUsername() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_USERNAME + " FROM " + TABLE_USER + " LIMIT 1", null);
        String username = null;
        if(cursor.moveToFirst()){
            username = cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME));
        }
        cursor.close();
        return username;
    }

    // ----------- SESSION METHODS -----------

    public boolean insertSession(String start, String end, long duration, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_START, start);
        values.put(COL_END, end);
        values.put(COL_DURATION, duration);
        values.put(COL_DATE, date);

        long result = db.insert(TABLE_SESSIONS, null, values);
        return result != -1;
    }

    public Cursor getAllSessions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SESSIONS + " ORDER BY id DESC", null);
    }

    public void deleteSession(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SESSIONS, "id=?", new String[]{String.valueOf(id)});
    }

    // ----------- TOTAL DURATION METHODS -----------

    // Vrne skupni čas učenja kot hh:mm:ss
    public String getTotalStudyTime() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_DURATION + ") as total FROM " + TABLE_SESSIONS, null);
        long totalSeconds = 0;
        if(cursor.moveToFirst()){
            totalSeconds = cursor.getLong(cursor.getColumnIndexOrThrow("total"));
        }
        cursor.close();

        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }
}