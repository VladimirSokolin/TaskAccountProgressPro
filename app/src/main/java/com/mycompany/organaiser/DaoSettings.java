package com.mycompany.organaiser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class DaoSettings {

  private MyDatabaseOpenHelper dataHelper;
  public DaoSettings(Context context){
    dataHelper = new MyDatabaseOpenHelper(context);
  }
  public DaoSettings(MyDatabaseOpenHelper myDatabaseOpenHelper){
    dataHelper = myDatabaseOpenHelper;
  }



  public long insert(Setting setting){
    SQLiteDatabase db = dataHelper.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(Setting.TITLE, setting.title);
    values.put(Setting.DESCRIPTION, setting.description);
    values.put(Setting.VALUE, setting.value);
    return db.insert(Setting.NAME_TABLE, null, values);
  }

  public void update(Setting setting){
    SQLiteDatabase db = dataHelper.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(Setting.TITLE, setting.title);
    values.put(Setting.DESCRIPTION, setting.description);
    values.put(Setting.VALUE, setting.value);
    db.update(Setting.NAME_TABLE, values, Setting.ID + "=?", new String[]{String.valueOf(setting.id)});
  }

  public Setting getByTitle(String title){
    SQLiteDatabase db = dataHelper.getReadableDatabase();
    Cursor cursor = db.query(Setting.NAME_TABLE, null, Setting.TITLE + "=?", new String[]{title}, null, null, null);
    if(cursor.moveToNext()){
      Setting setting = new Setting();
      setting.id = cursor.getInt(cursor.getColumnIndexOrThrow(Setting.ID));
      setting.title = cursor.getString(cursor.getColumnIndexOrThrow(Setting.TITLE));
      setting.description = cursor.getString(cursor.getColumnIndexOrThrow(Setting.DESCRIPTION));
      setting.value = cursor.getInt(cursor.getColumnIndexOrThrow(Setting.VALUE));
      return setting;
    }
    return null;
  }

  public ArrayList<Setting> getAll(){
    SQLiteDatabase db = dataHelper.getReadableDatabase();
    Cursor cursor = db.query(Setting.NAME_TABLE, null, null, null, null, null, null);
    ArrayList<Setting> list = new ArrayList<Setting>();
    while(cursor.moveToNext()){
      Setting setting = new Setting();
      setting.id = cursor.getInt(cursor.getColumnIndexOrThrow(Setting.ID));
      setting.title = cursor.getString(cursor.getColumnIndexOrThrow(Setting.TITLE));
      setting.description = cursor.getString(cursor.getColumnIndexOrThrow(Setting.DESCRIPTION));
      setting.value = cursor.getInt(cursor.getColumnIndexOrThrow(Setting.VALUE));
      list.add(setting);
    }
    return list;
  }
}
