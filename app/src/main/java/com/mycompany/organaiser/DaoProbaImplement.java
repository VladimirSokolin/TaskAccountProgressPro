package com.mycompany.organaiser;
import android.database.sqlite.*;
import android.database.Cursor;
import android.content.*;

import java.util.*;

public class DaoProbaImplement implements DaoProba{
	
	MyDatabaseOpenHelper myDatabase;
	
	public DaoProbaImplement(Context context, String nameTable){
		myDatabase = new MyDatabaseOpenHelper(context);
		
	}
	
	public boolean createTable(){
		// TODO: Implement this method
		return false;
	}
	
	public void input(String text, int number ){
		SQLiteDatabase db = myDatabase.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("text", text);
		values.put("number", number);
		
		long right = db.insert("proba", null, values);
	}
	
	public ArrayList getCommit(Integer id){
		SQLiteDatabase db = myDatabase.getReadableDatabase();
		String selection = "id = ?";
		String[] selectionArgs = {id.toString()};
		
		Cursor cursor = db.query(
		"proba",
		null,
		selection,
		selectionArgs,
		null,
		null,
		null);
		
		ArrayList list = new ArrayList();
		while(cursor.moveToNext()){
			String el = cursor.getString(cursor.getColumnIndexOrThrow("text"));
			list.add(el);
		}
		cursor.close();
		return list;
		
	}
	
	public ArrayList readAll(){
		SQLiteDatabase db = myDatabase.getReadableDatabase();
	
		
		Cursor cursor = db.rawQuery("SELECT * FROM proba", null );
		
		ArrayList list = new ArrayList();
		while(cursor.moveToNext()){
			String str = cursor.getString(cursor.getColumnIndexOrThrow("text"));
			list.add(str);
		}
		cursor.close();
		return list;
	}
}
