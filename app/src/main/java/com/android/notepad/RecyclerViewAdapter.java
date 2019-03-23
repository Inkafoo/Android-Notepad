package com.android.notepad;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    Context context;
    DataBaseHelper mDatabaseHelper;
    List<NoteModel> noteModelList;

    public RecyclerViewAdapter(Context context, List<NoteModel> noteModelList) {

        this.context = context;
        this.noteModelList = noteModelList;
        mDatabaseHelper = new DataBaseHelper(context);

    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.view_single_note, viewGroup, false);

        return new RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder recyclerViewHolder, final int position) {

        final NoteModel noteModel = noteModelList.get(position);

        final int modelID = noteModel.getId();
        final String modelTitle = noteModel.getTitle();
        final String modelNote = noteModel.getNote();
        final String modelDate = noteModel.getDate();

        recyclerViewHolder.titleTxtView.setText(modelTitle);
        recyclerViewHolder.dateTxtView.setText("Last edit: " + modelDate);


        recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNoteModelData(recyclerViewHolder, modelID, modelTitle, modelNote, modelDate);
            }
        });

        recyclerViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteNoteAd(modelID, position);
                return false;
            }
        });

    }


    private void sendNoteModelData(RecyclerViewHolder recyclerViewHolder, int modelID, String modelTitle, String modelNote, String modelDate){
        Intent intent = new Intent(recyclerViewHolder.itemView.getContext(), NoteActivity.class);
        intent.putExtra("id", modelID);
        intent.putExtra("title", modelTitle);
        intent.putExtra("note", modelNote);
        intent.putExtra("date", modelDate);
        recyclerViewHolder.itemView.getContext().startActivity(intent);
    }


    private void deleteNoteAd(final int modelID, final int position){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.alert_delete_note, null);
        alertBuilder.setView(view);
        final AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        Button btn_no = alertDialog.findViewById(R.id.btn_no);
        Button btn_yes = alertDialog.findViewById(R.id.btn_yes);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper.deleteNote(modelID);
                noteModelList.remove(position);
                notifyItemRemoved(position);
                notifyItemChanged(position);
                notifyDataSetChanged();

                alertDialog.dismiss();
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


    @Override
    public int getItemCount() {
         return noteModelList.size();
    }

}
