package com.mycompany.organaiser;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import android.view.*;

public class ProgressActivity extends Activity {

	ListView listView;
	MyDatabaseOpenHelper dataHelper;
	DaoTask daoTask;
	ArrayList<Task> listCompleteTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_progress);
		
		dataHelper = new MyDatabaseOpenHelper(this);
		daoTask = new DaoTask(dataHelper);
		
	}
	
	class MyAdapter extends ArrayAdapter<Task>{
		
		MyAdapter(Context context, ArrayList list){
			super(context, R.layout.task_item, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		
			
			View view = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
			Task task  = getItem(position);
			TextView tv = view.findViewById(R.id.tv_name_task_main_activity);
			tv.setText(task.nameTask);
			
			return view;
		}
		
	}

	@Override
	protected void onResume()
	{
		update();
		super.onResume();
	}
	
	
	
	void update(){
		listCompleteTask = daoTask.getAllCompleteTask();
		listView = findViewById(R.id.list_complete_task_progress_activity);
		MyAdapter mAdapter = new MyAdapter(this, listCompleteTask);
		listView.setAdapter(mAdapter);
	}
	
}
