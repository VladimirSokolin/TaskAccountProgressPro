package com.mycompany.organaiser;
import android.content.*;
import android.database.sqlite.*;
import java.util.ArrayList;
import android.database.*;

public class DaoTaskLearn  {
	
	MyDatabaseOpenHelper dataHelper;
	String nameTable;
	String typeTable;
	
	public void DaoTaskLearn(Context context, String nameTable, String typeTable){
		
		dataHelper = new MyDatabaseOpenHelper(context);
		
		this.nameTable = nameTable;
		this.typeTable = typeTable;
		
	}
	
	public boolean addTask(Task task){
		SQLiteDatabase db = dataHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("dateStart", task.dateStart);
		values.put("dateEndPlane", task.dateEndPlane);
		values.put("description", task.description);
		values.put("fullCount", task.fullCount);
		values.put("currentCount", task.currentCount);
		values.put("countInDay", task.countInDay);
		values.put("dayToComplete", task.dayToComlete);
		
		long right = db.insert(nameTable, null, values);
		
		db.close();
		
		if(right != -1) return true;
		else return false;
	}
	
	public ArrayList<Task> getAllTask(){
		SQLiteDatabase db = dataHelper.getReadableDatabase();
		ArrayList<Task> list = new ArrayList<>();
		Cursor cursor = db.rawQuery("SELECT * FROM " + nameTable, null);
		
		while(cursor.moveToNext()){
			Task task = new Task();
		}
		return null;
	}
	
	
}
