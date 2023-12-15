package com.mycompany.organaiser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Timekeeper {
    public static final String ID = "id";
    public static final String TIME_START = "timeStart";
    public static final String ID_TASK = "idTask";
    public static final String TYPE_TASK = "typeTask";
    public static final String IS_IN_PROCESS = "isInProcess";

    public static final String timeNameTable = "timekeeperTable";
    public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + timeNameTable + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
    + TIME_START + " INTEGER, " + ID_TASK + " INTEGER, " + IS_IN_PROCESS + " BOOLEAN, " + TYPE_TASK + " INTEGER)";
    public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + timeNameTable;

    public long id;
    public long timeStart;
    public long idTask;

    public int typeTask;
    public boolean isInProcess;
    daoTimekeeper dao;

    public Timekeeper(Context context) {
        dao = new daoTimekeeper(new MyDatabaseOpenHelper(context));
        dao.getTimekeeper();
        if(id == -1){
            dao.addTimekeeper(this);
        }
    }


    class daoTimekeeper {
        MyDatabaseOpenHelper dataHelper;

        public daoTimekeeper(MyDatabaseOpenHelper dataHelper) {
            this.dataHelper = dataHelper;
        }

        public long addTimekeeper(Timekeeper timekeeper) {
            SQLiteDatabase db = dataHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TIME_START, timekeeper.timeStart);
            values.put(ID_TASK, timekeeper.idTask);
            values.put(IS_IN_PROCESS, timekeeper.isInProcess);
            values.put(TYPE_TASK, timekeeper.typeTask);
            timekeeper.id = (int) db.insert(timeNameTable, null, values);
            return id;
        }

        public void updateTimekeeper(Timekeeper timekeeper) {
            SQLiteDatabase db = dataHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TIME_START, timekeeper.timeStart);
            values.put(ID_TASK, timekeeper.idTask);
            values.put(IS_IN_PROCESS, timekeeper.isInProcess);
            values.put(TYPE_TASK, timekeeper.typeTask);
            db.update(timeNameTable, values, ID + "=" + timekeeper.id, null);
        }

        public void getTimekeeper() {
            SQLiteDatabase db = dataHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + timeNameTable + " WHERE id = 1", null);
            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
                    timeStart = cursor.getLong(cursor.getColumnIndexOrThrow(TIME_START));
                    idTask = cursor.getInt(cursor.getColumnIndexOrThrow(ID_TASK));
                    isInProcess = cursor.getInt(cursor.getColumnIndexOrThrow(IS_IN_PROCESS)) == 1;
                    typeTask = cursor.getInt(cursor.getColumnIndexOrThrow(TYPE_TASK));
                } while (cursor.moveToNext());
            } else {
                id = -1;
            }

        }
    }
}
