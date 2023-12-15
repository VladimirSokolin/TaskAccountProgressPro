package com.mycompany.organaiser;

public class ConstTimeSpace{
	
	public static final String TIME_START = "timeStart";
	public static final String TIME_STOP = "timeStop";

	public static final String ID = "id_time_space";
	public static final String NAME_DEAL = "nameDeal";
	public static final String DESCRIPTION ="description";
	public static final String COLOR = "color";
	public static final String DATE = "date";
	public static final String IS_FACT = "boolean";
	
	public static final String FOREIGN_KEY = "id_day_rout";
	
	public static final String NAME_TABLE = "tableOfTimeSpaces";
	
	public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " 
	+ NAME_TABLE + " (id_time_space INTEGER PRIMARY KEY, " + FOREIGN_KEY + " INTEGER, " + TIME_START + " INTEGER, " +IS_FACT+" BOOLEAN, " + TIME_STOP + " INTEGER, "
	+ NAME_DEAL + " TEXT, " + DESCRIPTION + " TEXT, " +  COLOR + " INTEGER, " + DATE + " TEXT, FOREIGN KEY ("+FOREIGN_KEY+") REFERENCES " + ConstDayRouting.NAME_TABLE + " ("+ ConstDayRouting.ID +") ON DELETE CASCADE)";
	
	public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + NAME_TABLE;
}
