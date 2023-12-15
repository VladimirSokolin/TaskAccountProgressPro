package com.mycompany.organaiser;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdapterFastNote extends RecyclerView.Adapter<AdapterFastNote.ViewHolder> {
    private List<FastNote> fastNotes;

    private OnClickNoteListener listener;
    private OnLongClickNoteListener longClickListener;
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
        View view = holder.itemView;
        TextView tvValue = view.findViewById(R.id.tv_fast_note_value);
        tvValue.setMovementMethod(new ScrollingMovementMethod());
        tvValue.setText(fastNotes.get(position).value);
        ImageView iv = view.findViewById(R.id.iv_item_fast_note_delete);

        tvValue.setOnClickListener((v)->{
            if(iv.getVisibility() == View.VISIBLE){
                iv.setVisibility(View.GONE);
            } else
            listener.onClickNote(fastNotes.get(position));
        });

        tvValue.setOnLongClickListener((v)->{
            longClickListener.onLongClickNote(fastNotes.get(position), view);
            return true;
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
    public void setOnLongClickNoteListener(OnLongClickNoteListener longClickListener){
        this.longClickListener = longClickListener;
    }

    public void setListOfFastNotes(List<FastNote> fastNotes) {
        this.fastNotes = fastNotes;
    }
}
