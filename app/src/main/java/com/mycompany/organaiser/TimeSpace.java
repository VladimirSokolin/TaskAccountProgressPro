package com.mycompany.organaiser;
import android.graphics.*;
import android.icu.text.*;
import android.icu.util.*;
import java.util.*;
import java.util.Calendar;

public class TimeSpace {

	public long id;
	String nameDeal;
	public String description;
	String date;
	int color = Color.RED;
	public boolean isFactTime = false;
	
	public String timeStart;
	public String timeStop;
	public String timeDelta;

	public long timeMlStart;
	public long timeMlStop;
	public long timeMlDelta;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	SimpleDateFormat sdfTimeOfDay = new SimpleDateFormat("HH:mm", Locale.getDefault());
	public TimeSpace(){
		
	}
	
	public TimeSpace(String nameDeal){
		this.nameDeal = nameDeal;
		date = sdf.format(new Date());
	}
	public TimeSpace(String nameDeal, long timeMlStart){
		this(nameDeal);
		this.timeMlStart = timeMlStart;
		timeStart = conversationMlToString(timeMlStart);
		
		
	}
	public TimeSpace(String nameDeal, long timeMlStart, long timeMlStop){
		this(nameDeal, timeMlStart);
		this.timeMlStop = timeMlStop;
		
		
		timeMlDelta = timeMlStop - timeMlStart;
	
		timeStop = conversationMlToString(timeMlStop);
		timeDelta = conversationMlToString(timeMlDelta);
	}
	

	
	public String conversationMlToString(long timeMl){
		SimpleDateFormat sdfTimeOfDay = new SimpleDateFormat("HH:mm", Locale.getDefault());
	
		String time = sdfTimeOfDay.format(new Date(timeMl));
		
		return time;
	}
	
	public long conversationStringToMl(String timeString){
		String[] parseString = timeString.split(":",0);
		int hour = Integer.valueOf(parseString[0]);
		int minute = Integer.valueOf(parseString[1]);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		
		return calendar.getTimeInMillis();
	}
	
	public void setColor(int color){
		this.color = color;
	}
	
	public void setDate(Date dateObj){
		
		String date = sdf.format(dateObj);
		this.date = date;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	
	
	public String getDate(){
		return date;
	}
	public void setStartMlTime(long startMlTime){
		this.timeMlStart = startMlTime;
		this.timeStart = conversationMlToString(startMlTime);
	}
	
	public void setStopMlTime(long stopMlTime){
		this.timeMlStop = stopMlTime;
		this.timeStop = conversationMlToString(stopMlTime);
		
	}
	
	public void setNameDeal(String deal){
		this.nameDeal = deal;
	}
	
	public void setStartTimeString(String start){
		this.timeStart = start;
		this.timeMlStart = conversationStringToMl(start);
	}
	
	public void setStopTimeString(String stop){
		this.timeStop = stop;
		this.timeMlStop = conversationStringToMl(stop);
	}
	

	
	
}
