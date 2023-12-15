package com.mycompany.organaiser;
import android.database.sqlite.*;
import android.content.*;
import java.util.*;
import android.database.*;

import java.text.*;

public class DaoDayRouting {
	
	MyDatabaseOpenHelper dataHelper;
	DaoTimeSpace daoTimeSpace;
	ConstDayRouting c;
	
	public DaoDayRouting(MyDatabaseOpenHelper dataHelper){
		this.dataHelper = dataHelper;
		daoTimeSpace = new DaoTimeSpace(dataHelper);
	}
	
	public long add(DayRouting dayRouting){
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		
		ContentValues v = new ContentValues();
		v.put(c.DATE, dayRouting.date);
		v.put(c.IS_TEMPLATE, dayRouting.isTamplate());
		
		long id = db.insert(c.NAME_TABLE, null, v);
		
		return id;
	}
	
	public ArrayList<DayRouting> getTemplates(){
		SQLiteDatabase db = dataHelper.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT * FROM " + c.NAME_TABLE + " WHERE "
		+ c.IS_TEMPLATE + " = true", null);
		
		ArrayList<DayRouting> list = new ArrayList<>();
		
		while(cur.moveToNext()){
			long id = cur.getLong(cur.getColumnIndexOrThrow(c.ID));
			DayRouting dayRouting = new DayRouting(id,
					cur.getString(cur.getColumnIndexOrThrow(c.DATE)),
					daoTimeSpace.getAllTimeSpaceOfDay(id)
							);
			dayRouting.setTamplate(true);
			
			//dayRouting.id = cur.getLong(cur.getColumnIndexOrThrow(c.ID));
			//dayRouting.date = cur.getString(cur.getColumnIndexOrThrow(c.DATE));
			//dayRouting.isTamplate = true;
			
			list.add(dayRouting);
		}
		return list;
	}
	
	public ArrayList<DayRouting> getDayRoutingOfDate(Date date){
		SQLiteDatabase db = dataHelper.getReadableDatabase();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		String formatDate = sdf.format(date);
		
		Cursor cur = db.rawQuery("SELECT * FROM " + c.NAME_TABLE + " WHERE "
								 + c.DATE + " = '"+formatDate+"'", null);

		ArrayList<DayRouting> list = new ArrayList<>();

		while(cur.moveToNext()){
			DayRouting dayRouting = new DayRouting();

			dayRouting.id = cur.getLong(cur.getColumnIndexOrThrow(c.ID));
			dayRouting.date = cur.getString(cur.getColumnIndexOrThrow(c.DATE));

			list.add(dayRouting);
		}
		return list;
	}

	public DayRouting getDayRouting(long id){
		SQLiteDatabase db = dataHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + c.NAME_TABLE + " WHERE " + c.ID + " = " + id, null);

		DayRouting dayRouting = new DayRouting();
		cursor.moveToNext();
		dayRouting.id = cursor.getLong(cursor.getColumnIndexOrThrow(c.ID));
		dayRouting.date = cursor.getString(cursor.getColumnIndexOrThrow(c.DATE));
		return dayRouting;
	}

	public Cursor getCursorAllDayRouting(){
		SQLiteDatabase db = dataHelper.getReadableDatabase();
		return db.rawQuery("SELECT * FROM " + c.NAME_TABLE, null);
	}
}
