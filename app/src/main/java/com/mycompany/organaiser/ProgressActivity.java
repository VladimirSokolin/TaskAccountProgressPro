package com.mycompany.organaiser;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import android.view.*;

public class ProgressActivity extends Activity {

	GridView gridView;
	MyDatabaseOpenHelper dataHelper;
	DaoTask daoTask;
	DaoSettings daoSettings;
	ArrayList<Task> listCompleteTask;

	Button btMenu;
	Button btTracker;
	Button btNotePad;
	Button btSettings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_progress);

		initNavigateButtons();

		dataHelper = new MyDatabaseOpenHelper(this);
		daoTask = new DaoTask(dataHelper);
		daoSettings = new DaoSettings(dataHelper);

		int color = daoSettings.getByTitle("color").value;

		getWindow().getDecorView().setBackgroundColor(color);
		getWindow().setStatusBarColor(color);
		getWindow().setNavigationBarColor(color);

	}
	
	class MyAdapter extends ArrayAdapter<Task>{
		
		MyAdapter(Context context, ArrayList list){
			super(context, R.layout.item_achive, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		
			
			View view = LayoutInflater.from(getContext()).inflate(R.layout.item_achive, parent, false);
			Task task  = getItem(position);
			TextView tvTitle = view.findViewById(R.id.textView);
			TextView tvCount = view.findViewById(R.id.textView2);
			if(task != null){
				tvTitle.setText(task.nameTask);
				tvCount.setText(String.valueOf(task.currentCount));
			}

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
		gridView = findViewById(R.id.list_complete_task_progress_activity);
		MyAdapter mAdapter = new MyAdapter(this, listCompleteTask);
		gridView.setAdapter(mAdapter);
	}

	void initNavigateButtons(){
		btSettings = findViewById(R.id.bt_navigation_settings);
		btSettings.setOnClickListener(view ->{
			startActivity(new Intent(this, SettingsActivity.class));
			finish();
		});

		btMenu = findViewById(R.id.bt_navigation_menu);
		btMenu.setOnClickListener(view ->{
			startActivity(new Intent(this, MainActivity.class));
			finish();
		});

		btTracker = findViewById(R.id.bt_navigation_tracker);
		btTracker.setOnClickListener((view)->{
			startActivity(new Intent(this, CommitActivity.class));
			finish();
		});
	}


	
}
