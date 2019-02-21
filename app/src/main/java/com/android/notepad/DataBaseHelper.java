package com.android.notepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "notepad_databasee";

    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_NOTE = "note";
    private static final String COL_DATE = "date";

    private SQLiteDatabase sqLiteDatabase;

    /**
     * create database
     * .execSQL use without concerning its output (e.g create/alter tables)
     * .rawQuery use when you expect some outputs (e.g date, select records)
     */

    public DataBaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        String createTable = "CREATE TABLE " +TABLE_NAME+ " (" +COL_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +COL_TITLE+ " TEXT, " +COL_NOTE+ " TEXT, " +COL_DATE+ " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String upgradeTable = "DROP TABLE " + TABLE_NAME;
        db.execSQL(upgradeTable);

        onCreate(db);
    }


    private SQLiteDatabase getSqLiteDatabase() {
        if(sqLiteDatabase == null){
            sqLiteDatabase = this.getWritableDatabase();
        }
        return sqLiteDatabase;
    }

    public boolean addData(String title, String note, String date){
        ContentValues contentValues = new ContentValues();
        //.put(where, what)
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_NOTE, note);
        contentValues.put(COL_DATE, date);

        long result = getSqLiteDatabase().insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = getSqLiteDatabase().rawQuery(query, null);

        return data;
    }

    public int getRowsCount(){
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME;
        Cursor data = getSqLiteDatabase().rawQuery(query, null);
        int count = data.getCount();
        //data.close();

        return count;
    }


























}
