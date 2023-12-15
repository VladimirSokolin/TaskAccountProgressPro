package com.mycompany.organaiser;

public class ConstDayRouting {
	public static final String ID = "id_day_routing";
	public static final String DATE = "date";
	public static final String IS_TEMPLATE = "isTemplate";
	public static final String NAME_TABLE = "dayRoutingTable";
	
	public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + NAME_TABLE + " (id_day_routing INTEGER PRIMARY KEY, "
	+ DATE + " TEXT, " + IS_TEMPLATE + " BOOLEAN)";
	
	public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + NAME_TABLE;
}
