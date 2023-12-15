package com.mycompany.organaiser;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class DaoFastNote {

    MyDatabaseOpenHelper dataHelper;

    public DaoFastNote(MyDatabaseOpenHelper dataHelper){
        this.dataHelper = dataHelper;
    }

    public long insert(FastNote fastNote){
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FastNote.VALUE, fastNote.value);
        values.put(FastNote.TITLE, fastNote.title);
        long id = db.insert(FastNote.NAME_TABLE, null, values);
      /*  for(RemindersFastNote remindersFastNote : fastNote.reminders){
            if(remindersFastNote.date == 0){
                throw new IllegalArgumentException("remindersFastNote.date == 0");
            }
            remindersFastNote.idFastNote = id;
            insertReminders(remindersFastNote);
        }*/
        return id;
    }

    public void update(FastNote fastNote){
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FastNote.VALUE, fastNote.value);
        values.put(FastNote.TITLE, fastNote.title);
        db.update(FastNote.NAME_TABLE, values, FastNote.ID + " = ?", new String[]{String.valueOf(fastNote.id)});
    }

    public FastNote get(long id){
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        FastNote fastNote = null;
        String[] columns = {FastNote.ID, FastNote.VALUE, FastNote.TITLE};
        String selection = FastNote.ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(FastNote.NAME_TABLE, columns, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()){
            fastNote = new FastNote();
            fastNote.id = cursor.getLong(0);
            fastNote.value = cursor.getString(1);
            fastNote.title = cursor.getString(2);
        }
        cursor.close();
        return fastNote;
    }

    public void delete(long id){
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        String selection = FastNote.ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        db.delete(FastNote.NAME_TABLE, selection, selectionArgs);
    }

    public ArrayList<FastNote> getAll(){
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        ArrayList<FastNote> fastNotes = new ArrayList<FastNote>();
        String[] columns = {FastNote.ID, FastNote.VALUE, FastNote.TITLE};
        Cursor cursor = db.query(FastNote.NAME_TABLE, columns, null, null, null, null, null);
        while(cursor.moveToNext()){
            FastNote fastNote = new FastNote();
            fastNote.id = cursor.getLong(0);
            fastNote.value = cursor.getString(1);
            fastNote.title = cursor.getString(2);
            fastNotes.add(fastNote);
        }
        cursor.close();
        return fastNotes;
    }

    public ArrayList<FastNote> getAllById(ArrayList<Long> ids){
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        ArrayList<FastNote> fastNotes = new ArrayList<FastNote>();
        String selection = FastNote.ID + " IN (" + String.join(",", Collections.nCopies(ids.size(), "?")) + ")";
        String[] selectionArgs = new String[ids.size()];
        for(int i = 0; i < ids.size(); i++){
            selectionArgs[i] = String.valueOf(ids.get(i));
        }
        String[] columns = {FastNote.ID, FastNote.VALUE, FastNote.TITLE};
        Cursor cursor = db.query(FastNote.NAME_TABLE, columns, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                FastNote fastNote = new FastNote();
                fastNote.id = cursor.getLong(0);
                fastNote.value = cursor.getString(1);
                fastNote.title = cursor.getString(2);
                fastNotes.add(fastNote);
            }
            cursor.close();
        }
        return fastNotes;
    }


}
