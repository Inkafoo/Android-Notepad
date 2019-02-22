package com.android.notepad;

public class NoteModel {

    public  String title, note, date;

   public NoteModel(String title, String note, String date) {
       this.title = title;
       this.note = note;
       this.date = date;
   }

    public String getTitle() {
        return title;
    }

    public void setTilie(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
