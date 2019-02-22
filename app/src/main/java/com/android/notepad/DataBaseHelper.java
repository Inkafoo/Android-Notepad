package com.android.notepad;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "notepad_database";

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



    public List<NoteModel> getdata(){

        List<NoteModel> data = new ArrayList<>();
        String query = "SELECT * FROM " +TABLE_NAME;
        Cursor cursor = getSqLiteDatabase().rawQuery(query,null);
        StringBuffer stringBuffer = new StringBuffer();


        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE));
            NoteModel noteModel = new NoteModel(title, note, date);
            stringBuffer.append(noteModel);
            data.add(noteModel);
        }


        return data;
    }








    public Cursor getWholeDatabase(){
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = getSqLiteDatabase().rawQuery(query, null);

        return data;
    }

    public Cursor getTitle(){
        String query = "SELECT " + COL_TITLE + " FROM " + TABLE_NAME;
        Cursor titleRow = getSqLiteDatabase().rawQuery(query, null);

        return titleRow;
    }

    public Cursor getNote(){
        String query = "SELECT " + COL_NOTE + " FROM " + TABLE_NAME;
        Cursor noteRow = getSqLiteDatabase().rawQuery(query, null);

        return noteRow;
    }

    public Cursor getDate(){
        String query = "SELECT " + COL_DATE + " FROM " + TABLE_NAME;
        Cursor dateRow = getSqLiteDatabase().rawQuery(query, null);

        return dateRow;
    }

    public int getRowsCount(){
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME;
        Cursor data = getSqLiteDatabase().rawQuery(query, null);
        int count = data.getCount();
        //data.close();

        return count;
    }











}
