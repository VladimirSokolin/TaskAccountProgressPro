package com.mycompany.organaiser;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DialogFastNoteCreate {
    Context context;
    FastNoteManager noteManager;
    AdapterDialogFastNoteTime adapterDialogFastNoteTime;
    AdapterUpdateable listenerOk;
    boolean isRedaction = false;
    FastNote fastNote;
    ArrayList<RemindersFastNote> reminders;
    int yearV, monthV, dayV;
    public DialogFastNoteCreate(Context context, FastNoteManager noteManager){
        this.context = context;
        this.noteManager = noteManager;
    }

    public DialogFastNoteCreate(Context context, FastNoteManager noteManager, FastNote fastNote){
        this(context, noteManager);
        this.fastNote = fastNote;
    }

    public void showDialog(){
        Dialog dialog = new Dialog(context);
        //set my layout without dialog
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_fast_note_create, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(contentView);
        if(dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        // установить такую ширину, при которой от краев экрана будет расстояние 20 dp
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels - 2 * margin;
        dialog.getWindow().setAttributes(layoutParams);

        LinearLayout linear = (LinearLayout) contentView.findViewById(R.id.layout_dialog_fast_note);
        if(linear != null) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(context.getResources().getColor(R.color.item_task_main));
            gradientDrawable.setCornerRadius(30);
            linear.setBackground(gradientDrawable);
        }
        TextView tvDate = contentView.findViewById(R.id.tv_dialog_fast_note_date);
        EditText etValue = contentView.findViewById(R.id.et_dialog_fast_note_value);
        Button btOk = contentView.findViewById(R.id.bt_ok_dialog_fast_note);
        Button btCancel = contentView.findViewById(R.id.bt_cancel_dialog_fast_note);
        GridView gvTimes = contentView.findViewById(R.id.grid_view_dialog_fast_note_times);

        Calendar calendar = Calendar.getInstance();
        dayV = calendar.get(Calendar.DAY_OF_MONTH);
        monthV = calendar.get(Calendar.MONTH)+1;
        yearV = calendar.get(Calendar.YEAR);


        tvDate.setText(String.format("%d.%d.%d", dayV, monthV, yearV));
        reminders = new ArrayList<>();
        if(fastNote == null){
            fastNote = new FastNote();
            Date startDate = calendar.getTime();
            RemindersFastNote remindersFastNote = new RemindersFastNote();
            remindersFastNote.setDate(startDate.getTime());
            fastNote.reminders.add(remindersFastNote);
        } else {
            etValue.setText(fastNote.value);
            isRedaction = true;
        }
        reminders = fastNote.reminders;
        adapterDialogFastNoteTime = new AdapterDialogFastNoteTime(context,reminders);
        adapterDialogFastNoteTime.setOnItemReminderDeleteListener((reminder)->{
            reminders.remove(reminder);
        });
        gvTimes.setAdapter(adapterDialogFastNoteTime);

        tvDate.setOnClickListener(v->{
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    int rawMonth = month;
                    month++;
                    if(month == 13) {
                        month = 1;
                    }
                    String dayStr = day < 10 ? "0" + day : "" + day;
                    String monthStr = month < 10 ? "0" + month : "" + month;
                    tvDate.setText(dayStr + "." + monthStr + "." + year);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, rawMonth, day);
                    RemindersFastNote reminder = new RemindersFastNote();
                    reminder.setDate(calendar.getTimeInMillis());
                    reminders.add(reminder);
                    adapterDialogFastNoteTime.notifyDataSetChanged();
                }
            }, yearV, monthV-1, dayV
            );
            datePickerDialog.show();
        });


        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRedaction){
                    noteManager.delete(fastNote.id);
                }
                fastNote.value = String.valueOf(etValue.getText());
                fastNote.reminders = reminders;
                noteManager.insert(fastNote);
                listenerOk.updateAdapter(fastNote, isRedaction);
                dialog.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void setOnUpdateListener(AdapterUpdateable adapter){
        this.listenerOk = adapter;
    }
}
