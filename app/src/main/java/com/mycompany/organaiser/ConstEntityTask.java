package com.mycompany.organaiser;
import com.mycompany.organaiser.Entity;

import java.util.*;

public class ConstEntityTask extends Entity {
	
	static final String NAME_TASK = "nameTask";
	static final String COLOR = "color";
	static final String DATE_START  = "dateStart";
	static final String DATE_END_PLANE  = "dateEndPlane";
	static final String DESCRIPTION  = "description";
	static final String FULL_COUNT   = "fullCount";
	static final String CURRENT_COUNT  = "currentCount";
	static final String COUNT_IN_DAY   = "countInDay";
	static final String DAY_TO_COMPLETE   = "dayToComplete";
	static final String IS_COMPLETE = "isComplete";
	
	static final String taskNameTable = "tasks";
	
	static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + taskNameTable + " (id_task INTEGER PRIMARY KEY, " 
	+ NAME_TASK + " TEXT, " + DATE_START + " TEXT, " + DATE_END_PLANE + " TEXT, " + DESCRIPTION 
	+ " TEXT, " + FULL_COUNT + " INTEGER, " + CURRENT_COUNT + " INTEGER, "+COLOR + " INTEGER, " + COUNT_IN_DAY + " TEXT, " + DAY_TO_COMPLETE + " INTEGER, " + IS_COMPLETE + " BOOLEAN)";
	
	static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + taskNameTable;

	/*
	public ConstEntityTask(){
		
		nameOfTable = taskNameTable;
		
		listTitlesOfColumns = new ArrayList<>();
		listTitlesOfColumns.add(new Column(NAME_TASK, "TEXT"));
		listTitlesOfColumns.add(new Column(DATE_START, "TEXT"));
		listTitlesOfColumns.add(new Column(DATE_END_PLANE, "TEXT"));
		listTitlesOfColumns.add(new Column(DESCRIPTION, "TEXT"));
		listTitlesOfColumns.add(new Column(FULL_COUNT, "INTEGER"));
		listTitlesOfColumns.add(new Column(CURRENT_COUNT, "INTEGER"));
		listTitlesOfColumns.add(new Column(COUNT_IN_DAY, "INTEGER"));
		listTitlesOfColumns.add(new Column(DAY_TO_COMPLETE, "INTEGER"));
	}
	*/
}
