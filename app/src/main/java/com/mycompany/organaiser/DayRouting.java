package com.mycompany.organaiser;
import java.util.*;
import android.icu.text.*;

public class DayRouting {
	
	long id;
	public static final String TEMPLATE = "template";
	String date;
	private boolean isTamplate = false;
	ArrayList<TimeSpace> listOfTimeSpaces;
	
	
	public DayRouting(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		date = sdf.format(new Date());
		listOfTimeSpaces = new ArrayList<TimeSpace>();
	}
	
	public DayRouting(long id, String date, ArrayList<TimeSpace> listOfTimeSpaces){
		this.id = id;
		this.date = date;
		this.listOfTimeSpaces = listOfTimeSpaces;
	}
	
	public void addTimeSpace(TimeSpace timeSpace){
		listOfTimeSpaces.add(timeSpace);
	}
	
	public void toDoTemplate(String nameTemplate){
		isTamplate = true;
		date = TEMPLATE + " " + nameTemplate;
	}

	public boolean isTamplate() {
		return isTamplate;
	}
	public boolean setTamplate(boolean isTamplate) {
		return this.isTamplate = isTamplate;
	}
}
