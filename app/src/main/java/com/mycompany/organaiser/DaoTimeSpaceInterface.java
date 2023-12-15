package com.mycompany.organaiser;
import java.util.ArrayList;
import android.text.method.*;

import com.mycompany.organaiser.TimeSpace;

public interface DaoTimeSpaceInterface{
		public long addTimeSpace(TimeSpace timeSpace, long id);
		public TimeSpace getTimeSpace(long id);
		public ArrayList<TimeSpace> getAllTimeSpaceOfDay(long idDayRouting);
		public boolean update(TimeSpace timeSpace);
		public boolean delete(long id);
	
}
