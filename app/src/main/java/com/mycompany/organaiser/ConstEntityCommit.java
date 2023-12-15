package com.mycompany.organaiser;

public class ConstEntityCommit {
	
	public static final String FOREIGN_KEY = "id_task";
	public static final String DATE = "date";
	public static final String TIME_START = "timeStart";
	public static final String TIME_END = "timeEnd";
	public static final String TIME_DELTA = "timeDelta";
	public static final String PAGE_START = "pageStart";
	public static final String PAGE_END = "pageEnd";
	public static final String PAGE_DELTA = "pageDelta";
	public static final String DESCRIPTION = "description";
	public static final String LEVEL_ATTENTION = "levelAttention";
	public static final String PAGE_IN_TIME = "pageInTime";
	
	public static final String NAME_TABLE = "commitsOfTasks";
	
	public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + NAME_TABLE + " (id_commit INTEGER PRIMARY KEY, " + FOREIGN_KEY + " INTEGER, " 
	+ DATE + " TEXT, " + TIME_START + " TEXT, " + TIME_END + " TEXT, " + TIME_DELTA + " TEXT, "
	+ PAGE_START + " INTEGER, " + PAGE_END + " INTEGER, "  + PAGE_DELTA + " INTEGER, " + DESCRIPTION + " TEXT, " + LEVEL_ATTENTION + " INTEGER, " 
	+ PAGE_IN_TIME + " TEXT, FOREIGN KEY (" + FOREIGN_KEY + ") REFERENCES " + ConstEntityTask.taskNameTable + " ( id_task ) ON DELETE CASCADE )";
	
	public static final  String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + ConstEntityCommit.NAME_TABLE;
}
