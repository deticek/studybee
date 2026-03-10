package com.example.studybee;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "studybee.db";
    private static final int DATABASE_VERSION = 2;

    // ----------- SESSIONS TABLE -----------
    public static final String TABLE_SESSIONS = "sessions";
    public static final String COL_ID = "id";
    public static final String COL_START = "start_time";
    public static final String COL_END = "end_time";
    public static final String COL_DURATION = "duration";
    public static final String COL_DATE = "date";

    // ----------- USER TABLE -----------
    public static final String TABLE_USER = "User";
    public static final String COL_USERNAME = "username";
    public static final String COL_FOCUSMODE = "focus"; // 0/1 integer

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ----------- CREATE TABLES -----------
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Sessions table
        String CREATE_TABLE_SESSIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_SESSIONS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_START + " TEXT, " +
                COL_END + " TEXT, " +
                COL_DURATION + " INTEGER, " +
                COL_DATE + " TEXT)";
        db.execSQL(CREATE_TABLE_SESSIONS);

        // User table
        String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_USER +
                " (" + COL_USERNAME + " TEXT, " +
                COL_FOCUSMODE + " INTEGER)";
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    // ----------- USER METHODS -----------

    // Ensures there is always 1 row for user
    public void createDefaultUserIfNeeded() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " LIMIT 1", null);

        if(!cursor.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(COL_USERNAME, "User");
            values.put(COL_FOCUSMODE, 0);
            db.insert(TABLE_USER, null, values);
        }

        cursor.close();
    }

    // Set username
    public boolean setUsername(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);

        int rows = db.update(TABLE_USER, values, null, null);
        return rows > 0;
    }

    // Get username
    public String getUsername(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COL_USERNAME + " FROM " + TABLE_USER + " LIMIT 1",
                null
        );

        String username = null;
        if(cursor.moveToFirst()){
            username = cursor.getString(0);
        }

        cursor.close();
        return username;
    }

    // Set focus mode
    public boolean setFocusMode(boolean focus){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FOCUSMODE, focus ? 1 : 0);

        int rows = db.update(TABLE_USER, values, null, null);
        return rows > 0;
    }

    // Get focus mode
    public boolean getFocusMode(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COL_FOCUSMODE + " FROM " + TABLE_USER + " LIMIT 1",
                null
        );

        boolean focus = false;
        if(cursor.moveToFirst()){
            focus = cursor.getInt(0) == 1;
        }

        cursor.close();
        return focus;
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

    public String getTotalStudyTime() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_DURATION + ") as total FROM " + TABLE_SESSIONS, null);
        long totalSeconds = 0;
        if(cursor.moveToFirst()){
            totalSeconds = cursor.getLong(0);
        }
        cursor.close();

        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }
}