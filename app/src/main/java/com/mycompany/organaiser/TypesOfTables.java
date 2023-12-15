package com.mycompany.organaiser;

public enum TypesOfTables {
	
	TASK_LEARN("dateStart TEXT, dateEndPlane TEXT, description TEXT, fullCount INTEGER, currentCount INTEGER, countInDay INTEGER, dayToComplete INTEGER)"),
	COMMIT_READ("date TEXT, description TEXT, timeStart TEXT, timeEnd TEXT, timeDelta TEXT, pageStart INTEGER, pageEnd INTEGER, pageDelta INTEGER, pageInTime TEXT, levelAttention INTEGER)");
	
	String textOfQuery;
	TypesOfTables(String textOfQuery){
		this.textOfQuery = textOfQuery;
	}
}
