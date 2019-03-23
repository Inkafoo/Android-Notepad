package com.android.notepad;

public class NoteModel {

    public int id;
    public  String title, note, date;

   public NoteModel(int id, String title, String note, String date) {
       this.id = id;
       this.title = title;
       this.note = note;
       this.date = date;
   }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }


    public String getNote() {
        return note;
    }


    public String getDate() {
        return date;
    }

}
