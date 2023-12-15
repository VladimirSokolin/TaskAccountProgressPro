package com.mycompany.organaiser;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdapterFastNote extends RecyclerView.Adapter<AdapterFastNote.ViewHolder> {
    List<FastNote> fastNotes;

    OnClickNoteListener listener;
    public AdapterFastNote(List<FastNote> fastNotes) {
        this.fastNotes = fastNotes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(View.inflate(parent.getContext(), R.layout.item_fast_note, null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView tvValue = holder.itemView.findViewById(R.id.tv_fast_note_value);
        tvValue.setMovementMethod(new ScrollingMovementMethod());
        tvValue.setText(fastNotes.get(position).value);

        tvValue.setOnClickListener((view)->{
            listener.onClickNote(fastNotes.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return fastNotes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    public void setOnClickNoteListener(OnClickNoteListener listener){
        this.listener = listener;
    }

}
