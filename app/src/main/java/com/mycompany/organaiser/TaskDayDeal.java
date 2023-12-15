package com.mycompany.organaiser;
import android.graphics.*;

public class TaskDayDeal extends Task implements CompleteDealIntergace {
	
	
	public TaskDayDeal( String nameDeal){
		this.nameTask = nameDeal;
	}
	
	public void setColor(int color){
		this.color = color;
	}

	//метод играет роль старта 
	@Override
	public void compute(long timeStart){
		// dayGoOn вместо timeStart
		dayGoOn = timeStart;
	}
	
	@Override
	public TimeSpace completeDeal(){
		long timeStop = System.currentTimeMillis();
		
		TimeSpace timeSpace = new TimeSpace(nameTask, dayGoOn, timeStop);
		timeSpace.isFactTime = true;
		timeSpace.color = color;
		
		return timeSpace;
	}
	
}
