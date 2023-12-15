package com.mycompany.organaiser;
import java.text.ParseException;
import java.util.*;
import android.icu.text.*;
import java.util.concurrent.*;
import android.graphics.*;

public class Task implements CompleteDealIntergace {

	long id;
	
	boolean isComplete = false;
	
	String nameTask;
	String dateStart = null;
	String dateEndPlane = null;
	String description;
	
	double fullCount;
	double currentCount;
	double countInDay;
	double dayToComlete;
	
	int color = Color.RED;
	
	//добавил позже:
	private double remainCount;
	long dayGoOn;
	
	String refNameOfCommit;
	
	public void createRefNameOfCommit(){
		refNameOfCommit = nameTask + nameTask;
	}
	
	public String getRefNameOfCommit(){
		return refNameOfCommit;
	}

	
	public void compute(long startTime){
		remainCount = fullCount - currentCount;

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
		// set date start from startTime in format "dd.MM.yyyy"
		long timeStartTask;
		try {
			timeStartTask = sdf.parse(dateStart).getTime();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}


		try{
			long timeMillis = System.currentTimeMillis() - timeStartTask;
			dayGoOn = TimeUnit.DAYS.convert(timeMillis, TimeUnit.MILLISECONDS);
			if(dayGoOn == 0) dayGoOn = 1;
		} catch (Exception pe){
			pe.printStackTrace();
		}
		
		countInDay = currentCount / dayGoOn;
		if(Double.isInfinite(countInDay)) countInDay = 0;
		dayToComlete = remainCount / countInDay;
		if(Double.isInfinite(dayToComlete)) dayToComlete = 9999;
	}

	@Override
	public TimeSpace completeDeal()
	{
		// TODO: Implement this method
		return null;
	}
	
}
