package com.mycompany.organaiser;

import java.util.ArrayList;

public class FastNote {
    public static final String ID = "id";
    public static final String VALUE = "value";
    public static final String TITLE = "title";
    public static final String NAME_TABLE = "fast_note";
    public static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME_TABLE +
            " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + VALUE + " TEXT, " + TITLE + " TEXT);";
    public static final String QUERY_DROP_TABLE = "DROP TABLE IF EXISTS " + NAME_TABLE + ";";
    long id;
    String title;
    String value;
    ArrayList<RemindersFastNote> reminders;

    public FastNote(){
        reminders = new ArrayList<RemindersFastNote>();
    }
}
