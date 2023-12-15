package com.mycompany.organaiser;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterDialogFastNoteTime extends ArrayAdapter<RemindersFastNote> {

    interface OnItemReminderDeleteListener {
        void onItemReminderDelete(RemindersFastNote reminder);
    }
    OnItemReminderDeleteListener onItemReminderDeleteListener;

    public AdapterDialogFastNoteTime(Context context, ArrayList<RemindersFastNote> reminders) {
        super(context, R.layout.item_fast_note, reminders);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RemindersFastNote reminder = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_fast_note_reminder, parent, false);
        TextView tvDate = view.findViewById(R.id.tv_item_fast_note_reminder);
        ImageView ivDelete = view.findViewById(R.id.iv_item_fast_note_reminder_delite);
        tvDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(reminder.date));

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemReminderDeleteListener.onItemReminderDelete(reminder);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    public void setOnItemReminderDeleteListener(OnItemReminderDeleteListener onItemReminderDeleteListener) {
        this.onItemReminderDeleteListener = onItemReminderDeleteListener;
    }
}
