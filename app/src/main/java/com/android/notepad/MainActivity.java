package com.android.notepad;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * https://github.com/mitchtabian/SaveReadWriteDeleteSQLite/blob/master/SaveAndDisplaySQL/app/src/main/java/com/tabian/saveanddisplaysql/DatabaseHelper.java
 * ctrl + p = parameters method
 * https://dzone.com/articles/create-a-database-android-application-in-android-s tutorial dobre ej dobre
 */


public class MainActivity extends AppCompatActivity {

    Context context = this;
    DataBaseHelper dataBaseHelper;

    FloatingActionButton fab;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataBaseHelper = new DataBaseHelper(context);
        fab = findViewById(R.id.fab);
        mRecyclerView  = findViewById(R.id.list_recycler);

        loadDatabaseList();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             showNewNoteAlert();
            }
        });
    }

    private void loadDatabaseList() {

    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    mRecyclerAdapter = new RecyclerViewAdapter(context);














       //Cursor data = dataBaseHelper.getData();

       //ArrayList<String> listData = new ArrayList<>();
       //while(data.moveToNext()){
       //    listData.add(data.getString(0));
       //    listData.add(data.getString(1));
       //    listData.add(data.getString(2));
       //}

       //ListAdapter listAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, listData);
       //notesListView.setAdapter(listAdapter);
    }


    private void showNewNoteAlert() {
        //create alertDialog
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
        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy, HH:mm:ss");
        String todayDate = dateFormat.format(Calendar.getInstance().getTime());

        boolean insertData = dataBaseHelper.addData(title, note, todayDate);

        if(insertData){
            toastMessage("Data successfully inserted!");
            loadDatabaseList();
            return true;
        }else{
            toastMessage("Something went wrong");
            return false;
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }























}
