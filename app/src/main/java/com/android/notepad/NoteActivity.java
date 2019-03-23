package com.android.notepad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteActivity extends AppCompatActivity {

    Context context = this;
    DataBaseHelper mDatabaseHelper;
    boolean nightMode;
    MenuItem menuItem;

    EditText edtTitle, edtNote;
    LinearLayout contentLayout;

    int modelIdInt;
    String modelTitleString;
    String modelNoteString;
    String modelDateString;

    Menu myMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        //prepare layoutActivity
        Toolbar toolbar = findViewById(R.id.toolbar_note);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.overridePendingTransition(0, 0);

        SharedPreferences sharedPreferences = getSharedPreferences("SSP", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("nightModeBoolean", false);


        //find elements
        contentLayout = findViewById(R.id.content_layout);
        edtTitle = findViewById(R.id.activity_note_title);
        edtNote = findViewById(R.id.activity_note_context);
        menuItem = findViewById(R.id.menu_night_mode);
        mDatabaseHelper = new DataBaseHelper(context);

        //date from recyclerView
        Intent receivedIntent = getIntent();
        modelIdInt = receivedIntent.getIntExtra("id", 1);
        modelTitleString = receivedIntent.getStringExtra("title");
        modelNoteString = receivedIntent.getStringExtra("note");
        modelDateString = receivedIntent.getStringExtra("date");

        loadNoteDate(modelTitleString, modelNoteString);
        setNightMode();

    }

    private void loadNoteDate(String modelTitle, String modelNote) {
        edtTitle.setText(modelTitle);
        edtNote.setText(modelNote);

        edtTitle.setSelection(modelTitle.length());
        edtNote.setSelection(modelNote.length());
    }

    private void setNightMode(){
        if(nightMode == true){
            contentLayout.setBackgroundResource(R.color.night_mode);
            edtTitle.setTextColor(Color.parseColor("#FFC9C9C9"));
            edtNote.setTextColor(Color.parseColor("#FFC9C9C9"));
        }else{
            contentLayout.setBackgroundResource(R.color.background_main_activity);
            edtTitle.setTextColor(Color.parseColor("#000000"));
            edtNote.setTextColor(Color.parseColor("#000000"));
        }
    }

    /*** MENU METHODS */
    private void menuUpdate(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss, d MMM yyyy");
        String todayDate = dateFormat.format(Calendar.getInstance().getTime());

        String id = Integer.toString(modelIdInt);
        boolean result = mDatabaseHelper.updateNote(id, edtTitle.getText().toString(), edtNote.getText().toString(), todayDate);
        if(result == true){
            toastMessage("Note saved");
            backToMainActivity();
        }else{
            toastMessage("sproboj zapisac notatke pononwie");
        }
    }

    private void menuDelete(){
        mDatabaseHelper.deleteNote(modelIdInt);
    }

    private void checkChange(){
        if(edtNote.getText().toString().equals(modelNoteString) && edtTitle.getText().toString().equals(modelTitleString)){
            toastMessage("Nie wprowadzono zmian");
        }else{
            //update note
            menuUpdate();
        }
    }

    private void menuShare(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "mySubject");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, modelTitleString +"\n \n"+ modelNoteString);
        startActivity(Intent.createChooser(sharingIntent, "Share note"));
    }

    private void menuNightMode(){
        MenuItem menuItem = myMenu.findItem(R.id.menu_night_mode);

        if(menuItem.isChecked()){
            SharedPreferences sharedPreferences = getSharedPreferences("SSP", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("nightModeBoolean", false);
            editor.apply();
        }else{
            SharedPreferences sharedPreferences = getSharedPreferences("SSP", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("nightModeBoolean", true);
            editor.apply();
        }

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss, d MMM yyyy");
        String todayDate = dateFormat.format(Calendar.getInstance().getTime());

        String id = Integer.toString(modelIdInt);
        mDatabaseHelper.updateNote(id, edtTitle.getText().toString(), edtNote.getText().toString(), todayDate);

        finish();
        startActivity(getIntent());

    }

    private void backToMainActivity(){
        finish();
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    private void toastMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        myMenu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_save:
                checkChange();
                break;
            case R.id.menu_delete:
                menuDelete();
                backToMainActivity();
                break;
            case R.id.menu_back:
                loadNoteDate(modelTitleString, modelNoteString);
                break;
            case R.id.menu_share:
                menuShare();
                break;
            case R.id.menu_night_mode:
                menuNightMode();
                break;
            case android.R.id.home:
                menuUpdate();
                finish();
                this.overridePendingTransition(0,0);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(nightMode == true){
            menu.findItem(R.id.menu_night_mode).setChecked(true);
        }else{
            menu.findItem(R.id.menu_night_mode).setChecked(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        menuUpdate();
        super.onBackPressed();
        this.overridePendingTransition(0,0);
    }






















}
