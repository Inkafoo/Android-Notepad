package com.android.notepad;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


class RecyclerViewHolder extends RecyclerView.ViewHolder{

    public TextView titleTxtView, dateTxtView;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);


        titleTxtView = itemView.findViewById(R.id.note_title);
        dateTxtView = itemView.findViewById(R.id.note_date);
    }

}




public class RecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerViewHolder>{

    Context context;
    DataBaseHelper mDatabaseHelper;
    List<NoteModel> noteModelList;

    public RecyclerViewAdapter(Context context, List<NoteModel> noteModelList){

        this.context = context;
        this.noteModelList = noteModelList;
        mDatabaseHelper = new DataBaseHelper(context);

    }





    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.view_single_note, viewGroup, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {

        NoteModel noteModel = noteModelList.get(i);
        recyclerViewHolder.titleTxtView.setText(noteModel.getTitle());
        recyclerViewHolder.dateTxtView.setText(noteModel.getDate());
    }

    @Override
    public int getItemCount() {
         return noteModelList.size();
    }















}
