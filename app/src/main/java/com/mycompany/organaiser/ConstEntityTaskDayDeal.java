package com.mycompany.organaiser;

public class ConstEntityTaskDayDeal{
	public static final String NAME_DEAL= "nameDeal";
	public static final String ID = "idTaskDayTime";
	public static final String COLOR = "color";
	
	public static final String NAME_TABLE = "tableOfTaskDayDeal";
	
	public static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+NAME_TABLE+" (idTaskDayTime INTEGER PRIMARY KEY, "+NAME_DEAL+" TEXT, "
	+COLOR+" INTEGER)";
	
	public static final String QUERY_DROP_TABLE = "DROP TABLE IF EXISTS " + NAME_TABLE;
}
