package com.android.notepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "myDatabase";
    private static final String COL_ID = "id";          //col  0
    private static final String COL_TITLE = "title";    //col  1
    private static final String COL_NOTE = "note";      //col  2
    private static final String COL_DATE = "date";      //col

    private SQLiteDatabase sqLiteDatabase;


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
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void deleteNote(int id){
        String query = "DELETE FROM " +TABLE_NAME+ " WHERE " +COL_ID+ " == " + id ;
        getSqLiteDatabase().execSQL(query);
    }

    public boolean updateNote(String id, String title, String note, String date){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_NOTE, note);
        contentValues.put(COL_DATE, date);

        long result = getSqLiteDatabase().update(TABLE_NAME, contentValues, COL_ID +" = "+ id , null);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public List<NoteModel> getData(){

        List<NoteModel> data = new ArrayList<>();
        String query = "SELECT * FROM " +TABLE_NAME;
        Cursor cursor = getSqLiteDatabase().rawQuery(query,null);
        StringBuffer stringBuffer = new StringBuffer();


        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE));
            NoteModel noteModel = new NoteModel(id, title, note, date);
            stringBuffer.append(noteModel);
            data.add(0, noteModel);
        }


        return data;
    }

}
