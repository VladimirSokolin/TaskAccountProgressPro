package com.mycompany.organaiser;
import java.util.*;
import android.database.sqlite.*;
import android.content.*;
import android.database.*;

public class DaoTimeSpace implements DaoTimeSpaceInterface{
	MyDatabaseOpenHelper dataHelper;
	
	public DaoTimeSpace(MyDatabaseOpenHelper dataHelper){
		this.dataHelper = dataHelper;
	}

	@Override
	public long addTimeSpace(TimeSpace timeSpace, long idDayRouting)
	{
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		ContentValues v = new ContentValues();
		
		v.put(ConstTimeSpace.TIME_START, timeSpace.timeMlStart);
		v.put(ConstTimeSpace.TIME_STOP, timeSpace.timeMlStop);
		v.put(ConstTimeSpace.NAME_DEAL, timeSpace.nameDeal);
		v.put(ConstTimeSpace.DESCRIPTION, timeSpace.description);
		v.put(ConstTimeSpace.COLOR, timeSpace.color);
		v.put(ConstTimeSpace.DATE, timeSpace.date);
		v.put(ConstTimeSpace.IS_FACT, timeSpace.isFactTime);
		v.put(ConstTimeSpace.FOREIGN_KEY, idDayRouting);
		
		long id = db.insert(ConstTimeSpace.NAME_TABLE, null, v);
		db.close();
		
		return id;
	}

	@Override
	public ArrayList<TimeSpace> getAllTimeSpaceOfDay(long id)
	{
		SQLiteDatabase db = dataHelper.getReadableDatabase();
		
		ArrayList<TimeSpace> list = new ArrayList<>();
		
		Cursor cur = db.rawQuery("SELECT * FROM " + ConstTimeSpace.NAME_TABLE + " WHERE " + ConstTimeSpace.FOREIGN_KEY + " = " + id, null);
		while(cur.moveToNext()){
			
			String nameDeal = cur.getString(cur.getColumnIndexOrThrow(ConstTimeSpace.NAME_DEAL));
			long timeMlStart = cur.getLong(cur.getColumnIndexOrThrow(ConstTimeSpace.TIME_START));
			long timeMlStop = cur.getLong(cur.getColumnIndexOrThrow(ConstTimeSpace.TIME_STOP));
			String description = cur.getString(cur.getColumnIndexOrThrow(ConstTimeSpace.DESCRIPTION));
			String date = cur.getString(cur.getColumnIndexOrThrow(ConstTimeSpace.DATE));
			int color = cur.getInt(cur.getColumnIndexOrThrow(ConstTimeSpace.COLOR));
			
			
			TimeSpace ts = new TimeSpace(nameDeal, timeMlStart, timeMlStop);
			ts.setColor(color);
			ts.setDate(date);
			
			if(description != null){
				ts.description = description;
			}
			
			list.add(ts);
			
		}
		return list;
	}

	@Override
	public TimeSpace getTimeSpace(long id) {

		SQLiteDatabase db = dataHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + ConstTimeSpace.NAME_TABLE + " WHERE " + ConstTimeSpace.ID + " = " + id, null);
		cursor.moveToFirst();
		TimeSpace timeSpace = new TimeSpace(
				cursor.getString(cursor.getColumnIndexOrThrow(ConstTimeSpace.NAME_DEAL)),
				cursor.getLong(cursor.getColumnIndexOrThrow(ConstTimeSpace.TIME_START)),
				cursor.getLong(cursor.getColumnIndexOrThrow(ConstTimeSpace.TIME_STOP)));
		timeSpace.id = cursor.getLong(cursor.getColumnIndexOrThrow(ConstTimeSpace.ID));
		timeSpace.setColor(cursor.getInt(cursor.getColumnIndexOrThrow(ConstTimeSpace.COLOR)));
		timeSpace.setDate(cursor.getString(cursor.getColumnIndexOrThrow(ConstTimeSpace.DATE)));
		timeSpace.isFactTime = cursor.getInt(cursor.getColumnIndexOrThrow(ConstTimeSpace.IS_FACT)) == 1;
		timeSpace.description = cursor.getString(cursor.getColumnIndexOrThrow(ConstTimeSpace.DESCRIPTION));
		cursor.close();

		return timeSpace;
	}

	
	

	@Override
	public boolean update(TimeSpace timeSpace)
	{
		SQLiteDatabase db = dataHelper.getReadableDatabase();
		ContentValues v = new ContentValues();
		v.put(ConstTimeSpace.NAME_DEAL, timeSpace.nameDeal);
		v.put(ConstTimeSpace.TIME_START, timeSpace.timeMlStart);
		v.put(ConstTimeSpace.TIME_STOP, timeSpace.timeMlStop);
		v.put(ConstTimeSpace.DESCRIPTION, timeSpace.description);
		v.put(ConstTimeSpace.COLOR, timeSpace.color);
		v.put(ConstTimeSpace.DATE, timeSpace.date);
		v.put(ConstTimeSpace.IS_FACT, timeSpace.isFactTime);

		db.update(ConstTimeSpace.NAME_TABLE, v, ConstTimeSpace.ID + " = " + timeSpace.id, null);
		return false;
	}

	@Override
	public boolean delete(long id) {
		SQLiteDatabase db = dataHelper.getReadableDatabase();
		db.delete(ConstTimeSpace.NAME_TABLE, ConstTimeSpace.ID + " = " + id, null);
		return false;
	}

	public ArrayList<TimeSpace> getAllTimeSpaceOfIsFact(long id)
	{
		SQLiteDatabase db = dataHelper.getReadableDatabase();

		ArrayList<TimeSpace> list = new ArrayList<>();

		Cursor cur = db.rawQuery("SELECT * FROM " + ConstTimeSpace.NAME_TABLE + " WHERE " + ConstTimeSpace.FOREIGN_KEY + " = " + id + " AND " + ConstTimeSpace.IS_FACT + " = true", null);
		while(cur.moveToNext()){

			String nameDeal = cur.getString(cur.getColumnIndexOrThrow(ConstTimeSpace.NAME_DEAL));
			long timeMlStart = cur.getLong(cur.getColumnIndexOrThrow(ConstTimeSpace.TIME_START));
			long timeMlStop = cur.getLong(cur.getColumnIndexOrThrow(ConstTimeSpace.TIME_STOP));
			String description = cur.getString(cur.getColumnIndexOrThrow(ConstTimeSpace.DESCRIPTION));
			String date = cur.getString(cur.getColumnIndexOrThrow(ConstTimeSpace.DATE));
			int color = cur.getInt(cur.getColumnIndexOrThrow(ConstTimeSpace.COLOR));


			TimeSpace ts = new TimeSpace(nameDeal, timeMlStart, timeMlStop);
			ts.setColor(color);
			ts.setDate(date);
			ts.isFactTime = true;

			if(description != null){
				ts.description = description;
			}

			list.add(ts);

		}
		return list;
	}
	
	public ArrayList<TimeSpace> getAllTimeSpaceOfDayIsPlane(long id)
	{
		SQLiteDatabase db = dataHelper.getReadableDatabase();

		ArrayList<TimeSpace> list = new ArrayList<>();

		Cursor cur = db.rawQuery("SELECT * FROM " + ConstTimeSpace.NAME_TABLE + " WHERE " + ConstTimeSpace.FOREIGN_KEY + " = " + id + " AND " + ConstTimeSpace.IS_FACT + " = false ", null);
		while(cur.moveToNext()){

			long idTS = cur.getLong(cur.getColumnIndexOrThrow(ConstTimeSpace.ID));
			String nameDeal = cur.getString(cur.getColumnIndexOrThrow(ConstTimeSpace.NAME_DEAL));
			long timeMlStart = cur.getLong(cur.getColumnIndexOrThrow(ConstTimeSpace.TIME_START));
			long timeMlStop = cur.getLong(cur.getColumnIndexOrThrow(ConstTimeSpace.TIME_STOP));
			String description = cur.getString(cur.getColumnIndexOrThrow(ConstTimeSpace.DESCRIPTION));
			String date = cur.getString(cur.getColumnIndexOrThrow(ConstTimeSpace.DATE));
			int color = cur.getInt(cur.getColumnIndexOrThrow(ConstTimeSpace.COLOR));
	

			TimeSpace ts = new TimeSpace(nameDeal, timeMlStart, timeMlStop);
			ts.id = idTS;
			ts.setColor(color);
			ts.setDate(date);
			// isFactTime = false по-умолчанию

			if(description != null){
				ts.description = description;
			}

			list.add(ts);

		}
		return list;
	}
	
	
	public ArrayList<TimeSpace> getAllTimeSpaceOfDayIsFact( String currentDay)
	{
		SQLiteDatabase db = dataHelper.getReadableDatabase();

		ArrayList<TimeSpace> list = new ArrayList<>();

		Cursor cur = db.rawQuery("SELECT * FROM " + ConstTimeSpace.NAME_TABLE + " WHERE " + ConstTimeSpace.DATE + " = '" + currentDay +"' AND " + ConstTimeSpace.IS_FACT + " = true", null);
		while(cur.moveToNext()){

			String nameDeal = cur.getString(cur.getColumnIndexOrThrow(ConstTimeSpace.NAME_DEAL));
			long timeMlStart = cur.getLong(cur.getColumnIndexOrThrow(ConstTimeSpace.TIME_START));
			long timeMlStop = cur.getLong(cur.getColumnIndexOrThrow(ConstTimeSpace.TIME_STOP));
			String description = cur.getString(cur.getColumnIndexOrThrow(ConstTimeSpace.DESCRIPTION));
			String date = cur.getString(cur.getColumnIndexOrThrow(ConstTimeSpace.DATE));
			int color = cur.getInt(cur.getColumnIndexOrThrow(ConstTimeSpace.COLOR));


			TimeSpace ts = new TimeSpace(nameDeal, timeMlStart, timeMlStop);
			ts.setColor(color);
			ts.setDate(date);
			ts.isFactTime = true;

			if(description != null){
				ts.description = description;
			}

			list.add(ts);

		}
		return list;
	}

	public void deleteTimeSpacesOfDayRoutingIsPlane(long id){
		SQLiteDatabase db = dataHelper.getReadableDatabase();
		db.execSQL("DELETE FROM " + ConstTimeSpace.NAME_TABLE + " WHERE " + ConstTimeSpace.FOREIGN_KEY + " = " + id + " AND " + ConstTimeSpace.IS_FACT + " = false");
	}

	public void deleteTimeSpacesOfDayRoutingIsFact(long id){
		SQLiteDatabase db = dataHelper.getReadableDatabase();
		db.execSQL("DELETE FROM " + ConstTimeSpace.NAME_TABLE + " WHERE " + ConstTimeSpace.FOREIGN_KEY + " = " + id + " AND " + ConstTimeSpace.IS_FACT + " = true");
	}

	
}
