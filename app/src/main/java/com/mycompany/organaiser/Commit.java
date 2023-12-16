package com.mycompany.organaiser;
import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.view.GestureDetector.*;

public class Commit{
	
	String date;
	
	String timeStart;
	String timeEnd;
	String timeDelta;
	
	long timeStartMS;
	long timeEndMS;
	
	int levelAttention;
	
	double pageStart;
	double pageEnd;
	double pageDelta;
	
	String pageInTime;
	
	String description = null;
	
	boolean isStop = false;
	boolean isCompleate = false;
	
	Date systemStopDate;
	
	public Commit(){
		// for create obj from database
	}
	
	public Commit(int pageStart, long timeStartMS){
		
		this.pageStart = pageStart;

		date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(timeStartMS);
		timeStart = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(timeStartMS);
		this.timeStartMS = timeStartMS;
	}
	
	public void toStopTimer(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
		timeEndMS = System.currentTimeMillis();
		timeEnd = sdf.format(timeEndMS);
		long timeDeltaMS = timeEndMS - timeStartMS;
		timeDelta = sdf.format(timeDeltaMS);
		/* Old solution, but I'll leave it here
		   because it's useful tool:

		long seconds = (timeDeltaMS/1000) % 60;
		long minutes = (timeDeltaMS/(1000 * 60))%60;
		long hours = (timeDeltaMS/(1000 * 360));

		timeDelta = String.format("%d : %d : %d ",hours, minutes,seconds);
		*/
		pageInTime = String.format("%.3f",(pageDelta / (timeDeltaMS/3_600_000)));
		isStop = true;
	}
	
	public Date getDateStop() throws Exception{
		if(!isStop) {
			throw new Exception();
			// you must firstly to invoke toStopTimer();
		}
		return systemStopDate;
	}
	
	public void toCompleate(double pageEnd, String description, int levelAttention){
		this.pageEnd = pageEnd;
		
		pageDelta = pageEnd - pageStart;

		this.description = description;
		this.levelAttention = levelAttention;
		isCompleate = true;
	}
	
}
