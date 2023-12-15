package com.mycompany.organaiser;

public class RemindersFastNote {

    public static final String ID = "id";
    public static final String ID_NOTE = "id_note";
    public static final String DATE = "date";
    public static final String NAME_TABLE = "reminders_fast_note";
    public static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME_TABLE + " (" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ID_NOTE + " INTEGER, " + DATE + " INTEGER, FOREIGN KEY ("
            + ID_NOTE + ") REFERENCES " + FastNote.NAME_TABLE + "(" + FastNote.ID + ") ON UPDATE CASCADE ON DELETE CASCADE);";
    public static final String QUERY_DROP_TABLE = "DROP TABLE IF EXISTS " + NAME_TABLE;


    long id;
    long idFastNote;
    long date;

    public void setDate(long date){
        this.date = date;
    }
}
