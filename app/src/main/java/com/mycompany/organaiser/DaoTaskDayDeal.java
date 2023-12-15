package com.mycompany.organaiser;
import android.content.*;
import android.database.sqlite.*;
import android.database.*;

import java.util.*;

public class DaoTaskDayDeal{
	
	MyDatabaseOpenHelper dataHelper;
	ConstEntityTaskDayDeal ct = new ConstEntityTaskDayDeal();
	
	public DaoTaskDayDeal(MyDatabaseOpenHelper dataHelper){
		this.dataHelper = dataHelper;
	}
	
	public long add(TaskDayDeal taskDayDeal){
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ct.NAME_DEAL, taskDayDeal.nameTask);
		values.put(ct.COLOR, taskDayDeal.color);
		
		long id = db.insert(ct.NAME_TABLE, null, values);
		return id;
	}

	public TaskDayDeal get(long id){
		SQLiteDatabase db = dataHelper.getReadableDatabase();

		Cursor c = db.rawQuery("SELECT * FROM "+ct.NAME_TABLE+" WHERE "+ct.ID+"=?", new String[]{String.valueOf(id)});

		if(c.moveToFirst()){
			String name = c.getString(c.getColumnIndexOrThrow(ct.NAME_DEAL));
			TaskDayDeal deal = new TaskDayDeal(name);
			deal.id = c.getInt(c.getColumnIndexOrThrow(ct.ID));
			deal.color = c.getInt(c.getColumnIndexOrThrow(ct.COLOR));

			return deal;
		}

		return null;
	}
	
	public ArrayList getAll(){
		SQLiteDatabase db = dataHelper.getReadableDatabase();
		
		Cursor c = db.rawQuery("SELECT * FROM "+ct.NAME_TABLE, null);
		
		ArrayList<TaskDayDeal> list = new ArrayList<>();
		while(c.moveToNext()){
			String name = c.getString(c.getColumnIndexOrThrow(ct.NAME_DEAL));
			TaskDayDeal deal = new TaskDayDeal(name);
			deal.id = c.getInt(c.getColumnIndexOrThrow(ct.ID));
			deal.color = c.getInt(c.getColumnIndexOrThrow(ct.COLOR));
			
			list.add(deal);
		}
		
		return list;
	}

	public void update(TaskDayDeal taskDayDeal){
		SQLiteDatabase db = dataHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(ct.NAME_DEAL, taskDayDeal.nameTask);
		values.put(ct.COLOR, taskDayDeal.color);

		db.update(ct.NAME_TABLE, values, ct.ID+" = ?", new String[]{String.valueOf(taskDayDeal.id)});
	}
	public void delete(TaskDayDeal taskDayDeal){
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		db.delete(ct.NAME_TABLE, ct.ID+" = ?", new String[]{String.valueOf(taskDayDeal.id)});
	}
	
}
