package com.android.notepad;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    Context context = this;
    DataBaseHelper dataBaseHelper;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mRecyclerAdapter;
    List<NoteModel> noteModelList;

    FloatingActionButton fab;
    RelativeLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //prepare layoutActivity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        this.overridePendingTransition(0, 0);

        SharedPreferences sharedPreferences = getSharedPreferences("SSP", MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("nightModeBoolean", false);


        //find elements
        mRecyclerView  = findViewById(R.id.list_recycler);
        fab = findViewById(R.id.fab);
        contentLayout = findViewById(R.id.main_layout_content);

        // create new
        noteModelList = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(context);


        //methods in onCreate
        loadDatabaseList();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             showNewNoteAlert();
            }
        });

        if(nightMode == true){
            contentLayout.setBackgroundResource(R.color.night_mode);
        }else{
            contentLayout.setBackgroundResource(R.color.background_main_activity);
        }
    }


    private void loadDatabaseList() {
         noteModelList = dataBaseHelper.getData();

         mRecyclerAdapter = new RecyclerViewAdapter(context, noteModelList);
         mRecyclerView.setHasFixedSize(true);
         mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
         mRecyclerView.setItemAnimator(new DefaultItemAnimator());
         mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    private void showNewNoteAlert() {
        //create, show alertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.alert_note, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();


        //find alertDialog view elements
        final EditText edtTitle = alertDialog.findViewById(R.id.edit_title);
        final EditText edtNote = alertDialog.findViewById(R.id.edit_note);
        Button btnCancel = alertDialog.findViewById(R.id.btn_cancel);
        Button btnSave = alertDialog.findViewById(R.id.btn_save);

        edtNote.setScroller(new Scroller(context));
        edtNote.setMaxLines(6);
        edtNote.setVerticalScrollBarEnabled(true);
        edtNote.setMovementMethod(new ScrollingMovementMethod());


        //handle listeners
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if(title.length() == 0 || note.length() == 0){
                    toastMessage("Enter title or note");
                }else{
                    if(addNewNote(title, note)){
                        alertDialog.dismiss();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private boolean addNewNote(String title, String note){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss, d MMM yyyy");
        String todayDate = dateFormat.format(Calendar.getInstance().getTime());

        boolean insertData = dataBaseHelper.addData(title, note, todayDate);

        if(insertData){
            toastMessage("Note successfully inserted!");
            loadDatabaseList();
            return true;
        }else{
            toastMessage("Something went wrong \n Try again ");
            return false;
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
