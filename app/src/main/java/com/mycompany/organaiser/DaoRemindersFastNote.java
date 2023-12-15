package com.mycompany.organaiser;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class DaoRemindersFastNote {
    MyDatabaseOpenHelper dataHelper;
    public DaoRemindersFastNote(MyDatabaseOpenHelper dataHelper){
        this.dataHelper = dataHelper;
    }

    public long insert(RemindersFastNote remindersFastNote){
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RemindersFastNote.DATE, remindersFastNote.date);
        values.put(RemindersFastNote.ID_NOTE, remindersFastNote.idFastNote);
        long id = db.insert(RemindersFastNote.NAME_TABLE, null, values);
        return id;
    }

    public void delete(long id){
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        String selection = RemindersFastNote.ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        db.delete(RemindersFastNote.NAME_TABLE, selection, selectionArgs);
    }

    public void deleteAllByIdNote(long id){
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        String selection = RemindersFastNote.ID_NOTE + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        db.delete(RemindersFastNote.NAME_TABLE, selection, selectionArgs);

    }

    public ArrayList<RemindersFastNote> getAllCurrentDate(){
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        ArrayList<RemindersFastNote> remindersFastNotes = new ArrayList<RemindersFastNote>();
        // raw query where date located between start of current day and end of current day
        Cursor cursor = db.rawQuery("SELECT * FROM " + RemindersFastNote.NAME_TABLE + " WHERE " +
                RemindersFastNote.DATE + " BETWEEN " + getStartOfDay() + " AND " +
                getEndOfDay(), null);
        while(cursor.moveToNext()){
            RemindersFastNote remindersFastNote = new RemindersFastNote();
            remindersFastNote.id = cursor.getLong(0);
            remindersFastNote.idFastNote = cursor.getLong(1);
            remindersFastNote.date = cursor.getLong(2);
            remindersFastNotes.add(remindersFastNote);
        }
        cursor.close();
        return remindersFastNotes;
    }

    public ArrayList<RemindersFastNote> getAllById(long id_note){
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        ArrayList<RemindersFastNote> remindersFastNotes = new ArrayList<RemindersFastNote>();
        String selection = RemindersFastNote.ID_NOTE + " = ?";
        String[] selectionArgs = {String.valueOf(id_note)};
        Cursor cursor = db.query(RemindersFastNote.NAME_TABLE, null, selection, selectionArgs, null, null, null);
        while(cursor.moveToNext()){
            RemindersFastNote remindersFastNote = new RemindersFastNote();
            remindersFastNote.id = cursor.getLong(0);
            remindersFastNote.idFastNote = cursor.getLong(1);
            remindersFastNote.date = cursor.getLong(2);
            remindersFastNotes.add(remindersFastNote);
        }
        cursor.close();
        return remindersFastNotes;
    }

    private long getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    private long getEndOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

}
