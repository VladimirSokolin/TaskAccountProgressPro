package com.mycompany.organaiser;
import java.util.*;

import android.annotation.SuppressLint;
import android.database.sqlite.*;
import android.content.*;
import android.database.*;

public class DaoOrganaiserLearn implements OrganaiserDao {
	
	static final String DATE = "date";
	static final String TIME_START = "timeStart";
	static final String TIME_END = "timeEnd";
	static final String TIME_DELTA = "timeDelta";
	static final String PAGE_START = "pageStart";
	
	MyDatabaseOpenHelper dataHelper;

	
	public DaoOrganaiserLearn(MyDatabaseOpenHelper dataHelper){
		this.dataHelper = dataHelper;
	}

	@Override
	public boolean update(Commit commit) {
		// TODO: Implement this method
		return false;
	}

	@Override
	public boolean add(Commit commit, long idTask)
	{
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(ConstEntityCommit.FOREIGN_KEY, idTask);
		values.put(DATE, commit.date);
		values.put("timeStart", commit.timeStart);
		values.put("timeEnd", commit.timeEnd);
		values.put("timeDelta", commit.timeDelta);
		values.put("pageStart", commit.pageStart);
		values.put("pageEnd", commit.pageEnd);
		values.put("pageDelta", commit.pageDelta);
		values.put("pageInTime", commit.pageInTime);
		values.put("levelAttention", commit.levelAttention);
		values.put("description", commit.description);
		
		long right = db.insert(ConstEntityCommit.NAME_TABLE, null, values);
		
		
		if(right != -1) {
			return true;
		} else return false;
	}

	@Override
	public Commit getElement(long id)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public ArrayList<Commit> getAll(long idTask)
	{
		SQLiteDatabase db = dataHelper.getReadableDatabase();
		ArrayList<Commit> list = new ArrayList<>();
		Cursor cursor = db.rawQuery("SELECT * FROM " +  ConstEntityCommit.NAME_TABLE + " WHERE id_task = " + idTask, null);
		while (cursor.moveToNext()){
			@SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DATE));
			String timeStart = cursor.getString(cursor.getColumnIndexOrThrow("timeStart"));
			String timeEnd = cursor.getString(cursor.getColumnIndexOrThrow("timeEnd"));
			String timeDelta = cursor.getString(cursor.getColumnIndexOrThrow("timeDelta"));
			String pageInTime = cursor.getString(cursor.getColumnIndexOrThrow("pageInTime"));
			String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
			int pageStart = cursor.getInt(cursor.getColumnIndexOrThrow("pageStart"));
			int pageEnd = cursor.getInt(cursor.getColumnIndexOrThrow("pageEnd"));
			int pageDelta = cursor.getInt(cursor.getColumnIndexOrThrow("pageDelta"));
			int levelAttention = cursor.getInt(cursor.getColumnIndexOrThrow("levelAttention"));
			
			Commit commit = new Commit();
			commit.date = date;
			commit.description = description;
			commit.levelAttention = levelAttention;
			commit.timeStart = timeStart;
			commit.timeEnd = timeEnd;
			commit.timeDelta = timeDelta;
			commit.pageStart = pageStart;
			commit.pageEnd = pageEnd;
			commit.pageDelta = pageDelta;
			commit.pageInTime = pageInTime;
			
			list.add(commit);
		}
		cursor.close();
		return list;
	}

	@Override
	public boolean delete(long id)
	{
		// TODO: Implement this method
		return false;
	}

	
	

	
	


	
	

	
	
}
