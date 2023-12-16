package com.mycompany.organaiser;
import java.util.*;

import android.database.sqlite.*;
import android.content.*;
import android.database.*;

public class DaoTask implements OrganaiserDaoTask {
	
	ConstEntityTask entity;
	String nameOfTable;
	MyDatabaseOpenHelper dataHelper;
	
	public DaoTask(MyDatabaseOpenHelper dataHelper){
		this.dataHelper = dataHelper;
		entity = new ConstEntityTask();
		nameOfTable = entity.taskNameTable;
	}

	@Override
	public boolean addTask(Task task)
	{
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(entity.NAME_TASK, task.nameTask);
		values.put(entity.COLOR, task.color);
		values.put(entity.DATE_START, task.dateStart);
		values.put(entity.DATE_END_PLANE, task.dateEndPlane);
		values.put(entity.DESCRIPTION, task.description);
		values.put(entity.COUNT_IN_DAY, task.countInDay);
		values.put(entity.FULL_COUNT, task.fullCount);
		values.put(entity.CURRENT_COUNT, task.currentCount);
		values.put(entity.DAY_TO_COMPLETE, task.dayToComlete);
		values.put(entity.IS_COMPLETE, task.isComplete);
		
		long resultNumber = db.insert(nameOfTable, null, values);
		db.close();
		
		if(resultNumber != -1) return true;
		else return false;
	}

	@Override
	public ArrayList<Task> getAllTask() {
		SQLiteDatabase db = dataHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + nameOfTable + " WHERE " + entity.IS_COMPLETE+ " = false", null);
		ArrayList<Task> list = new ArrayList<>();
		while(cursor.moveToNext()){
			Task t = new Task();
			t.id = cursor.getInt(cursor.getColumnIndexOrThrow("id_task"));
			t.nameTask = cursor.getString(cursor.getColumnIndexOrThrow(entity.NAME_TASK));
			t.color = cursor.getInt(cursor.getColumnIndexOrThrow(entity.COLOR));
			t.dateStart = cursor.getString(cursor.getColumnIndexOrThrow(entity.DATE_START));
			t.dateEndPlane = cursor.getString(cursor.getColumnIndexOrThrow(entity.DATE_END_PLANE));
			t.description = cursor.getString(cursor.getColumnIndexOrThrow(entity.DESCRIPTION));
			t.countInDay = cursor.getDouble(cursor.getColumnIndexOrThrow(entity.COUNT_IN_DAY));
			t.fullCount = cursor.getDouble(cursor.getColumnIndexOrThrow(entity.FULL_COUNT));
			t.currentCount = cursor.getDouble(cursor.getColumnIndexOrThrow(entity.CURRENT_COUNT));
			t.dayToComlete = cursor.getDouble(cursor.getColumnIndexOrThrow(entity.DAY_TO_COMPLETE));
			
			list.add(t);
		}
		db.close();
		return list;
	}
	
	public ArrayList<Task> getAllCompleteTask(){
		SQLiteDatabase db = dataHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + nameOfTable + " WHERE " + entity.IS_COMPLETE+ " = true", null);
		ArrayList<Task> list = new ArrayList<>();
		while(cursor.moveToNext()){
			Task t = new Task();
			t.id = cursor.getInt(cursor.getColumnIndexOrThrow("id_task"));
			t.nameTask = cursor.getString(cursor.getColumnIndexOrThrow(entity.NAME_TASK));
			t.color = cursor.getInt(cursor.getColumnIndexOrThrow(entity.COLOR));
			t.dateStart = cursor.getString(cursor.getColumnIndexOrThrow(entity.DATE_START));
			t.dateEndPlane = cursor.getString(cursor.getColumnIndexOrThrow(entity.DATE_END_PLANE));
			t.description = cursor.getString(cursor.getColumnIndexOrThrow(entity.DESCRIPTION));
			t.countInDay = cursor.getDouble(cursor.getColumnIndexOrThrow(entity.COUNT_IN_DAY));
			t.fullCount = cursor.getDouble(cursor.getColumnIndexOrThrow(entity.FULL_COUNT));
			t.currentCount = cursor.getDouble(cursor.getColumnIndexOrThrow(entity.CURRENT_COUNT));
			t.dayToComlete = cursor.getDouble(cursor.getColumnIndexOrThrow(entity.DAY_TO_COMPLETE));
			t.isComplete = true;

			list.add(t);
		}
		db.close();
		return list;
	}

	@Override
	public Task getTask(long id)
	{
		SQLiteDatabase db = dataHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + nameOfTable + " WHERE id_task = " + id, null);
		Task t = new Task();
		if(cursor.moveToFirst()){
			do {
				t.id = cursor.getInt(cursor.getColumnIndexOrThrow("id_task"));
				t.nameTask = cursor.getString(cursor.getColumnIndexOrThrow(entity.NAME_TASK));
				t.color = cursor.getInt(cursor.getColumnIndexOrThrow(entity.COLOR));
				t.dateStart = cursor.getString(cursor.getColumnIndexOrThrow(entity.DATE_START));
				t.dateEndPlane = cursor.getString(cursor.getColumnIndexOrThrow(entity.DATE_END_PLANE));
				t.description = cursor.getString(cursor.getColumnIndexOrThrow(entity.DESCRIPTION));
				t.countInDay = cursor.getDouble(cursor.getColumnIndexOrThrow(entity.COUNT_IN_DAY));
				t.fullCount = cursor.getDouble(cursor.getColumnIndexOrThrow(entity.FULL_COUNT));
				t.currentCount = cursor.getDouble(cursor.getColumnIndexOrThrow(entity.CURRENT_COUNT));
				t.dayToComlete = cursor.getDouble(cursor.getColumnIndexOrThrow(entity.DAY_TO_COMPLETE));
				t.isComplete = cursor.getInt(cursor.getColumnIndexOrThrow(entity.IS_COMPLETE)) == 1;
			} while (cursor.moveToNext());
			return t;
		}
		return null;
	}

	

	@Override
	public boolean update(Task task) {
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(entity.NAME_TASK, task.nameTask);
		values.put(entity.COLOR, task.color);
		values.put(entity.DATE_START, task.dateStart);
		values.put(entity.DATE_END_PLANE, task.dateEndPlane);
		values.put(entity.DESCRIPTION, task.description);
		values.put(entity.COUNT_IN_DAY, task.countInDay);
		values.put(entity.FULL_COUNT, task.fullCount);
		values.put(entity.CURRENT_COUNT, task.currentCount);
		values.put(entity.DAY_TO_COMPLETE, task.dayToComlete);
		values.put(entity.IS_COMPLETE, task.isComplete);
		db.update(nameOfTable, values, "id_task = " + task.id, null);
		return false;
	}
	
	public void updateRegular(long id, double currentCount, double countInDay, double dayToComplete, boolean isComplete){
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		
		db.execSQL("UPDATE " + nameOfTable + " SET " + ConstEntityTask.CURRENT_COUNT + " = " 
		+ currentCount + ", " + ConstEntityTask.COUNT_IN_DAY + " = " + countInDay + ", " + ConstEntityTask.DAY_TO_COMPLETE + " = "
		+ dayToComplete + ", " +ConstEntityTask.IS_COMPLETE + " = " + isComplete + " WHERE id_task = " + id);
		db.close();
	}

	@Override
	public boolean delete(long id)
	{
		// TODO: Implement this method
		return false;
	}


}
