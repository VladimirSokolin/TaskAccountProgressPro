package com.mycompany.organaiser;
import android.content.res.AssetManager;
import android.database.sqlite.*;
import android.content.Context;
import android.os.Build;

import java.io.BufferedReader;
import java.io.IOException;

public class MyDatabaseOpenHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static int DATABASE_VERSION = 12;
    private final Context context;
    public static final String DATABASE_NAME = "FeedReader.db";
	
    public MyDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
	
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(ConstEntityTask.CREATE_TABLE_QUERY);
		    db.execSQL(ConstEntityCommit.CREATE_TABLE_QUERY);
		    db.execSQL(ConstTimeSpace.CREATE_TABLE_QUERY);
		    db.execSQL(ConstDayRouting.CREATE_TABLE_QUERY);
		    db.execSQL(ConstEntityTaskDayDeal.QUERY_CREATE_TABLE);
        db.execSQL(Timekeeper.CREATE_TABLE_QUERY);
        db.execSQL(FastNote.QUERY_CREATE_TABLE);
        db.execSQL(RemindersFastNote.QUERY_CREATE_TABLE);

        addSetting(db);
		 
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

    private void addSetting(SQLiteDatabase db) {
      db.execSQL(Setting.insertIfNotExist(new Setting("color","color of the all app", 0)));
      db.execSQL(Setting.insertIfNotExist(new Setting("time","time of time tracker", 0)));
      db.execSQL(Setting.insertIfNotExist(new Setting("space","space of time tracker", 5)));
    }
   
}
