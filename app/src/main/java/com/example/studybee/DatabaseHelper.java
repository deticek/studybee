package com.example.studybee;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "studybee.db";
    private static final int DATABASE_VERSION = 5;

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
    public static final String COL_MODE = "mode";
    public static final String COL_DEFULTTIME="defaulttime";
    public static final String COL_SOUND="sound";
    public static final String COL_BGMODE="bgmode";
    public static final String COL_BGSOUND="bgsound";

    // ------------ ACHIVMENTS TABLE -------------------

    public static final String TABLE_ACHIVEMENTS = "Achivements";
    public static final String COL_NAME = "name";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_UNLOCK = "unlock";
    public static final String COL_HIDDEN = "hidden";

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
                COL_FOCUSMODE + " INTEGER, "+
                COL_DEFULTTIME + " INTEGER DEFAULT 45, "+
                COL_SOUND + " INTEGER DEFAULT 1, "+
                COL_BGMODE + " INTEGER DEFAULT 0, "+
                COL_BGSOUND + " INTEGER DEFAULT 1, "+
                COL_MODE + " INTEGER)";
        db.execSQL(CREATE_TABLE_USER);

        // Achievements table
        String CREATE_TABLE_ACHIEVEMENTS = "CREATE TABLE IF NOT EXISTS " + TABLE_ACHIVEMENTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_UNLOCK + " INTEGER DEFAULT 0, " +
                COL_DATE + " TEXT,"+
                COL_HIDDEN + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_TABLE_ACHIEVEMENTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACHIVEMENTS);
        onCreate(db);
    }

    public void resetDb(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACHIVEMENTS);
        onCreate(db);
    }

    public void createDefaultInput() {
        createDefaultUserIfNeeded();
        insertAchievements(); // Call to insert achievements
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
            values.put(COL_MODE,0);
            values.put(COL_DEFULTTIME,45);
            values.put(COL_SOUND,1);
            values.put(COL_BGMODE,0);
            values.put(COL_BGSOUND,1);
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

    // defaulttime functions
    public boolean setDefultTime(int num){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DEFULTTIME, num);

        int rows = db.update(TABLE_USER, values, null, null);
        return rows > 0;
    }
    public int getDefaultTime(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COL_DEFULTTIME + " FROM " + TABLE_USER + " LIMIT 1",
                null
        );
        int focus = 45;
        if(cursor.moveToFirst()){
            focus = cursor.getInt(0);
        }
        cursor.close();
        return focus;
    }


    // sound functions
    public boolean setSound(int num){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SOUND, num);

        int rows = db.update(TABLE_USER, values, null, null);
        return rows > 0;
    }
    public int getSound(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COL_SOUND + " FROM " + TABLE_USER + " LIMIT 1",
                null
        );
        int focus = 45;
        if(cursor.moveToFirst()){
            focus = cursor.getInt(0);
        }
        cursor.close();
        return focus;
    }

    // bg noise sound functions

    public boolean setBgSound(int num){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BGSOUND, num);

        int rows = db.update(TABLE_USER, values, null, null);
        return rows > 0;
    }
    public int getBgSound(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COL_BGSOUND + " FROM " + TABLE_USER + " LIMIT 1",
                null
        );
        int focus = 45;
        if(cursor.moveToFirst()){
            focus = cursor.getInt(0);
        }
        cursor.close();
        return focus;
    }

    // bg sound mode functions

    public boolean setBgMode(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean focus = getBgMode();
        values.put(COL_BGMODE, focus ? 0 : 1);

        int rows = db.update(TABLE_USER, values, null, null);
        return rows > 0;
    }
    public boolean getBgMode(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COL_BGMODE + " FROM " + TABLE_USER + " LIMIT 1",
                null
        );

        boolean timer = false;
        if(cursor.moveToFirst()){
            timer = cursor.getInt(0) == 1;
        }

        cursor.close();
        return timer;
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

    // mode function

    public boolean setTimerMode(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean focus = getTimerMode();
        values.put(COL_MODE, focus ? 0 : 1);

        int rows = db.update(TABLE_USER, values, null, null);
        return rows > 0;
    }

    // Get focus mode
    public boolean getTimerMode(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COL_MODE + " FROM " + TABLE_USER + " LIMIT 1",
                null
        );

        boolean timer = false;
        if(cursor.moveToFirst()){
            timer = cursor.getInt(0) == 1;
        }

        cursor.close();
        return timer;
    }

    // --------- ACHIVEMENTS METHODS -------------

    private void insertAchievements() {

        SQLiteDatabase db = this.getWritableDatabase();

        Object[][] achievements = {
                {"First time seeing you", "Open the app for the first time.", 0}, //1 -
                {"Ready to focus", "Start the stopwatch for the first time.", 0}, //2 -
                {"One step forward", "Complete your first focus session.", 0}, //3 -

                {"Staying a while", "Focus for 5 minutes.", 0}, //4 -
                {"Getting into it", "Focus for 10 minutes.", 0}, //5 -
                {"Deep focus", "Focus for 25 minutes.", 0}, //6 -
                {"Halfway there", "Focus for 30 minutes.", 0}, //7 -
                {"One full hour", "Focus for 60 minutes.", 0}, //8 -

                {"Focus apprentice", "Complete 5 focus sessions.", 0}, //9 -
                {"Focus enjoyer", "Complete 10 focus sessions.", 0}, //10 -
                {"Productivity machine", "Complete 50 focus sessions.", 0},  //11 -

                {"Ready for focus", "Complete first focus with focus mode enabled.", 0}, //12 -
                {"Locked in", "Focus for 5 minutes with focus mode enabled.", 0},  //13 -
                {"Discipline", "Focus for 10 minutes with focus mode enabled.", 0}, //14 -
                {"Iron will", "Focus for 30 minutes with focus mode enabled.", 0}, //15 -
                {"Unbreakable focus", "Focus for 60 minutes with focus mode enabled.", 0}, //16 -

                {"Are you machine?", "Complete 100 focus sessions.", 1}, //17 -
                {"Insane focus", "Focus for 2 hours with focus mode enabled.", 1},  //18 -
                {"That was fast", "Stop the stopwatch on focuse mode after less than 5 seconds", 1}, //19 -
                {"Realy?", "Stop after 1 second", 1}, //20 -
                {"Are you studying or what?", "Focus for 2 hours.", 1}, //21 -
                {"Do you forget to turn off the timer?", "Focus for 3 hours or more.", 1}, //22 -
                {"That didn’t last long", "Stop the stopwatch after less than 10 seconds.", 1}, //23 -
                {"Distracted already", "Stop a session before reaching 1 minute.", 1}, //24 -
                {"Give up", "Leave the app whene the focuse mode is enable.", 1}, //25 -
        };

        for (Object[] achievement : achievements) {

            ContentValues values = new ContentValues();
            values.put(COL_NAME, (String) achievement[0]);
            values.put(COL_DESCRIPTION, (String) achievement[1]);
            values.put(COL_UNLOCK, 0);
            values.put(COL_HIDDEN, (Integer) achievement[2]);

            db.insert(TABLE_ACHIVEMENTS, null, values);
        }
    }

    public int getNumOfAchievements() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ACHIVEMENTS + " WHERE " + COL_UNLOCK + " = 1",null);

        int num = 0;

        if (cursor.moveToFirst()) {
            num = cursor.getInt(0);
        }

        cursor.close();

        return num;
    }

    public Cursor getAllAchivement() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ACHIVEMENTS + " ORDER BY id ASC", null);
    }

    public Cursor getAchivement(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+TABLE_ACHIVEMENTS+" WHERE id = "+id,null);
    }

    public void unlockAchievement(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        Date danes = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String formatiranDatum = format.format(danes);

        ContentValues values = new ContentValues();
        values.put("unlock", 1);
        values.put("date", formatiranDatum);

        db.update(TABLE_ACHIVEMENTS, values, "id=?", new String[]{String.valueOf(id)});
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

    public int getNumOfSessions(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT("+COL_ID+") FROM "+TABLE_SESSIONS,null);

        int count = 0;

        if (c != null) {
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            c.close();
        }

        return count;
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