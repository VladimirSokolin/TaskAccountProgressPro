package com.mycompany.organaiser;
import android.animation.ObjectAnimator;
import android.app.*;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.widget.*;
import android.content.*;
import android.view.*;
import java.util.*;
import android.widget.AdapterView.*;
import java.text.*;
import android.text.method.*;

import androidx.appcompat.app.AppCompatActivity;

import com.mycompany.organaiser.customView.TrackerView;


public class CommitActivity extends AppCompatActivity implements CommitCompleteInterface, DialogSetDayRoutingTemplate.SetRoutingDialogListener, AdapterUpdateable {
	
	String currentDay;
	TrackerView trackerView;
	ListAdapter mListAdapter;
	GridView mGridView;
	DaoTask daoTask;
	DaoTaskDayDeal daoDeal;
	DaoOrganaiserLearn daoCommit;
	DaoTimeSpace daoTimeSpace;
	DaoDayRouting daoDayRouting;
	DaoSettings daoSettings;

	Setting startHourTrackerSetting;
	Button btDealAdd;
	TextView tvTimer;
	TextView tvFactNote;
	TextView tvResume;

	Button btMenu;
	Button btTracker;
	Button btNotePad;
	Button btSettings;
	MyDatabaseOpenHelper dataHelper;
	boolean isPress = false;
	Commit commit = null;
	Handler handler;
	ArrayList<Task> listTasks;
	ArrayList<TaskDayDeal> listDeals;
	FragmentManager manager;
	Task task;
	Date currentDate;
	int idSendedTask;
	DayRouting currentDayRouting;
	DayRoutingPopupMenuInterface popupMenuDayRouting;
	int numberStartHour;

	TimeSpaceCommiter commiter;
	DialogConfirmationer confirmationer;
	boolean clickTaskDealForRedact = false;

	Timekeeper timekeeper;
	long startTime = -1;
	ArrayList<TimeSpace> listFactTimeSpaces;

	Handler invalidateTrackerHandler = new Handler(Looper.getMainLooper());

	boolean isUpdateTracker = true;
	@Override
	protected void onCreate(Bundle savedInstanceState){	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commit);

		timekeeper = new Timekeeper(this);

		currentDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		currentDay = sdf.format(currentDate);
		setTitle("Сэр, сегодня " + currentDay);
		commiter = new TimeSpaceCommiterImpl();
		confirmationer = new DialogConfirmationImpl(this);
		
		dataHelper = new MyDatabaseOpenHelper(this);
		daoTask = new DaoTask(dataHelper);
		daoDeal = new DaoTaskDayDeal(dataHelper);
		daoCommit = new DaoOrganaiserLearn(dataHelper);
		daoTimeSpace = new DaoTimeSpace(dataHelper);
		daoDayRouting = new DaoDayRouting(dataHelper);
		daoSettings = new DaoSettings(dataHelper);

		int colorSettings = daoSettings.getByTitle("color").value;
		startHourTrackerSetting = daoSettings.getByTitle("time");
		numberStartHour = startHourTrackerSetting.value;

		Window window = getWindow();
		window.setBackgroundDrawable(new ColorDrawable(colorSettings));
		window.setNavigationBarColor(colorSettings);
		window.setStatusBarColor(colorSettings);

		currentDayRouting = setCurrentDayRouting();

		trackerView = findViewById(R.id.tracker_view);
		trackerView.setHourStart(numberStartHour);
		tvTimer = findViewById(R.id.tv_timer);
		
		tvFactNote = findViewById(R.id.tv_fact_note);
		tvFactNote.setClipToOutline(true);
		tvFactNote.setMovementMethod(new ScrollingMovementMethod());
		
		tvResume = findViewById(R.id.tv_resume);
		tvResume.setClipToOutline(true);
		tvResume.setMovementMethod(new ScrollingMovementMethod());

		btTracker = findViewById(R.id.bt_navigation_tracker);
		Drawable drawableNavigateFocusButton = getDrawable(R.drawable.shape_view_navigate_focus);
		drawableNavigateFocusButton.setColorFilter(colorSettings, android.graphics.PorterDuff.Mode.SRC_IN);
		btTracker.setBackground(drawableNavigateFocusButton);
		btTracker.setTranslationY(-15);

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
		
		loadAndSetFactTimesCurrentDay(currentDay);

		
		manager = getFragmentManager();
		
		mGridView = findViewById(R.id.grid_layout_task);

		if(timekeeper.isInProcess ){
			if(timekeeper.typeTask == 1){
				task = daoTask.getTask(timekeeper.idTask);
			} else if (timekeeper.typeTask == 2){
				task = daoDeal.get(timekeeper.idTask);
			}
			startTime = timekeeper.timeStart;
			startProcessAccounting(task, startTime, null);
		}
		
		
		tvResume.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View view){
				DayRoutingPopupMenu popupMenu = new DayRoutingPopupMenu(CommitActivity.this, view, getFragmentManager());
				popupMenu.setIdCurrentDayRouting(currentDayRouting.id);
				popupMenuDayRouting = popupMenu;
				popupMenuDayRouting.showPopupMenuOfDayRoutingSet();
				return false;
			}
		});

		mGridView.setOnItemLongClickListener(new OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> l, View view, int position, long id){
				clickTaskDealForRedact = true;
				Task pickTask = (Task) l.getItemAtPosition(position);
				if(pickTask instanceof TaskDayDeal) {
					DealPopupMenu popupMenu = new DealPopupMenu(CommitActivity.this, view, getFragmentManager());
					popupMenu.setDeleteDealListener(()->{
						daoDeal.delete((TaskDayDeal) pickTask);
						listTasks.remove(pickTask);
						mGridView.invalidateViews();
					});
					popupMenu.setEditDealListener(()->{
						DayDealDialog dialog = new DayDealDialog();
						dialog.setTaskDayDeal((TaskDayDeal) pickTask);
						dialog.show(getFragmentManager(), "redactDeal");
					});

					popupMenu.show();


				} else if(pickTask instanceof SettingAdapterTask){
					DayDealDialog dialog = new DayDealDialog();
					dialog.show(getFragmentManager(), "newDeal");
				}else {
					Toast.makeText(CommitActivity.this, "The accounting task is edited in the main menu", Toast.LENGTH_LONG).show();
				}
				return false;
			}
		});
		
		
		mGridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> l, View view, int position, long id){
				Task t = (Task) l.getItemAtPosition(position);
				if( t instanceof SettingAdapterTask){
					DayDealDialog dialog = new DayDealDialog();
					dialog.show(getFragmentManager(), "newDeal");
				} else {
					if (!clickTaskDealForRedact) {
						if (!isPress) {
							startTime = System.currentTimeMillis();
							task = (Task) l.getItemAtPosition(position);
						}
						startProcessAccounting(task, startTime, view);
					}
					clickTaskDealForRedact = false;
				}
			}
		});

		tvTimer.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				showDialog();
			}
		});
		
	}

	@Override
	protected void onStart(){
		update();
		loadAndSetPlaneRoute();
		startInvalidatingTracker();
		super.onStart();
	}


	private void update(){

		listTasks = daoTask.getAllTask();
		listDeals = daoDeal.getAll();
		
		for(TaskDayDeal deal : listDeals){
			listTasks.add(deal);
		}
		SettingAdapterTask setting = new SettingAdapterTask(); //item for setting in grid view
		listTasks.add(setting);
		mListAdapter = new MyGridAdapter(this, listTasks);
		
		Intent intent = getIntent();
		if(intent != null){
			idSendedTask = intent.getIntExtra("id", -1);
		} 
		
		mGridView.setAdapter(mListAdapter);
	}
	
	
	private void removeCommitFromStore(){
		commit = null;
		System.gc();
	}
	
	public void clickFAB(View view){

		DayDealDialog dialog = new DayDealDialog();
		dialog.show(getFragmentManager(), "newDeal");
	}

	@Override
	public void onBackPressed()
	{
		startActivity(new Intent(this, MainActivity.class));
		super.onBackPressed();
	}

	private void showDialog(){
		Dialog dialog = new Dialog(this);
		//set my layout without dialog
		View contentView = getLayoutInflater().inflate(R.layout.dialog_complete_commit, null);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(contentView);
		if(dialog.getWindow() != null) {
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		}
		// установить такую ширину, при которой от краев экрана будет расстояние 20 dp
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.copyFrom(dialog.getWindow().getAttributes());
		int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
		layoutParams.width = getResources().getDisplayMetrics().widthPixels - 2 * margin;
		dialog.getWindow().setAttributes(layoutParams);

		TextView textTitle = contentView.findViewById(R.id.dialog_tv_start_page);
		textTitle.setText(getResources().getString(R.string.dialog_titel_commit_complete));

		EditText etPage = contentView.findViewById(R.id.et_new_current_page);
		//etPage.setHint((int)task.currentCount);
		EditText etDescription = contentView.findViewById(R.id.description_dialog_commit);
		Spinner etAttention = contentView.findViewById(R.id.spinner_attention);
		etAttention.setSelection(2);
		Button btOk = contentView.findViewById(R.id.bt_ok_dialog_complete);
		Button btCancel = contentView.findViewById(R.id.bt_cancel_dialog_complete);

		btOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String strPage = etPage.getText().toString();
				if(strPage.isEmpty()){
					strPage = "0.0";
				}
				String strAttention = etAttention.getSelectedItem().toString();
				String[] arrayAttention = getResources().getStringArray(R.array.level_attention);
				int dAttention;
				if(strAttention.equals(arrayAttention[0])){
					dAttention = 1;
				} else if(strAttention.equals(arrayAttention[1])){
					dAttention = 2;
				} else if(strAttention.equals(arrayAttention[2])){
					dAttention = 3;
				} else if(strAttention.equals(arrayAttention[3])){
					dAttention = 4;
				} else if(strAttention.equals(arrayAttention[4])){
					dAttention = 5;
				} else {
					dAttention = 0;
				}
				onCommitComplete(Double.parseDouble(strPage), etDescription.getText().toString(), dAttention);
				//close dialog
				dialog.dismiss();
			}
		});
		btCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.cancel();
			}
		});

		dialog.show();
	}


	@Override
	public void onCommitComplete(double page, String description, int levelAttention){
		
		if(commit != null) {
			commit.toCompleate(page, description, levelAttention);
			boolean isOk = daoCommit.add(commit, task.id);
			task.currentCount = page;
			task.compute(startTime);
			daoTask.updateRegular(task.id, task.currentCount, task.countInDay, task.dayToComlete, task.isComplete);
			
			TimeSpace timeSpaceFact = new TimeSpace(task.nameTask);
			timeSpaceFact.setStartTimeString(commit.timeStart);
			timeSpaceFact.setStopTimeString( commit.timeEnd );
			timeSpaceFact.description = description;
			timeSpaceFact.isFactTime = true;
			timeSpaceFact.color = task.color;
			
			daoTimeSpace.addTimeSpace(timeSpaceFact, currentDayRouting.id);
			listFactTimeSpaces.add(timeSpaceFact);
			
			commiter.commitTimeSpace(timeSpaceFact, tvFactNote);
			
			if(isOk) Toast.makeText(this,"Data successfully saved", Toast.LENGTH_SHORT).show();
			mGridView.invalidateViews();
			removeCommitFromStore();
			//commit.toCompleate(page, description, levelAttention);
			
		}
	}

	void loadAndSetPlaneRoute(long idDayRouting){
		
		ArrayList<TimeSpace> list = daoTimeSpace.getAllTimeSpaceOfDayIsPlane(idDayRouting);
		tvResume.setText("");
		for(TimeSpace ts : list){
			commiter.commitTimeSpace(ts, tvResume);
		}
	}

	void setPlaneRoute(){
		if(currentDayRouting != null){
			tvResume.setText("");
			for(TimeSpace ts : currentDayRouting.listOfTimeSpaces){
				commiter.commitTimeSpace(ts, tvResume);
			}
		}
	}
	
	void loadAndSetPlaneRoute(){


		if(currentDayRouting != null){
			currentDayRouting.listOfTimeSpaces = daoTimeSpace.getAllTimeSpaceOfDayIsPlane(currentDayRouting.id);
			trackerView.setTimeSpacesPlane(currentDayRouting.listOfTimeSpaces);
			tvResume.setText("");
			commiter.rangeAndCommitAllTimeSpaces(currentDayRouting.listOfTimeSpaces, tvResume);
		}

	}
	
	void loadAndSetFactTimesCurrentDay(String currentDay){
		
		listFactTimeSpaces = daoTimeSpace.getAllTimeSpaceOfDayIsFact(currentDay);
		trackerView.setTimeSpacesFact(listFactTimeSpaces);
		for(TimeSpace ts : listFactTimeSpaces){
			commiter.commitTimeSpace(ts, tvFactNote);
		}
	}

	@Override
	public void setRouting(DayRouting dayRouting){
		confirmationer.showDialogConfirmation("Confifmation!",
				"The new schedule will be installed instead of the old one. Continue?", "Yes", "No",
				new InnerDialogRunner() {
					@Override
					public void start() {
						daoTimeSpace.deleteTimeSpacesOfDayRoutingIsPlane(currentDayRouting.id);
						currentDayRouting.listOfTimeSpaces.clear();
						currentDayRouting.listOfTimeSpaces = daoTimeSpace.getAllTimeSpaceOfDayIsPlane(dayRouting.id);
                		for(TimeSpace ts : currentDayRouting.listOfTimeSpaces){
                    		daoTimeSpace.addTimeSpace(ts, currentDayRouting.id);
                		}
                		setPlaneRoute();
					}
				});

	}

	public DayRouting setCurrentDayRouting(){
		//Before creating a new DayRouting, you need to check whether
		// a new one has already been created for today’s date.
		// If created, then use it, if not, then create a new one

		ArrayList<DayRouting> dayRoutingList = daoDayRouting.getDayRoutingOfDate(currentDate);

		if(dayRoutingList.isEmpty()){
		    // Create a new DayRouting
		    DayRouting newDayRouting = new DayRouting();
		    // Set the date of the new DayRouting to today's date
		    newDayRouting.date = currentDay;
		    // Save the new DayRouting to the database
		    newDayRouting.id = daoDayRouting.add(newDayRouting);

		    return newDayRouting;
		} else {
		    // Use the existing DayRouting for today's date
		    return dayRoutingList.get(0);
		}

	}

	@Override
	public void updateAdapter(Object task, boolean isRedact){
		if(!isRedact){
			listTasks.add(listTasks.size()-1,(Task)task);
		}
		mGridView.invalidateViews();
	}

	private void startProcessAccounting(Task task,long startTime, View view ){

		if (isPress) {
			if (task instanceof TaskDayDeal) {
				TimeSpace ts = task.completeDeal();
				ts.setColor(task.color);
				daoTimeSpace.addTimeSpace(ts, currentDayRouting.id);
				commiter.commitTimeSpace(ts, tvFactNote);
			} else {
				commit.toStopTimer();
				showDialog();
			}
			timekeeper.isInProcess = false;
			timekeeper.dao.updateTimekeeper(timekeeper);
			isPress = false;
			if (view != null) {
				view.setBackgroundColor(getResources().getColor(android.R.color.white));
			}
		} else {

			task.compute(startTime); // первый подсчет
			tvTimer.setText(task.nameTask);
			if(view != null) {
				view.setBackgroundColor(getResources().getColor(android.R.color.tertiary_text_light));

			}
			if (!(task instanceof TaskDayDeal)) {
				commit = new Commit((int) task.currentCount, startTime);
			}
			isPress = true;

			// Save data of start time:
			timekeeper.isInProcess = true;
			if(task instanceof TaskDayDeal){
				timekeeper.typeTask = 2;
			} else {
				timekeeper.typeTask = 1;
			}
			timekeeper.timeStart = startTime;
			timekeeper.idTask = task.id;
			timekeeper.dao.updateTimekeeper(timekeeper);


			Runnable runnable = new Runnable() {
				public void run() {
					while (isPress) {
						try {
							Thread.sleep(500);
						} catch (Exception e) {
							e.printStackTrace();
						}
						Message msg = handler.obtainMessage();
						Bundle bundle = new Bundle();

						long deltaTime = System.currentTimeMillis() - startTime;


						long seconds = (deltaTime / 1000) % 60;
						long minutes = (deltaTime / (1000 * 60)) % 60;
						long hours = (deltaTime / (1000 * 3600)) % 60;

						String timer = String.format("%02d : %02d : %02d", hours, minutes, seconds);

						bundle.putString("t", timer);
						msg.setData(bundle);
						handler.sendMessage(msg);
							/*Message msg = handler.obtainMessage();
							Bundle bundle = new Bundle();
							SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
							String time = sdf.format(new Date());
							bundle.putString("t",time);
							msg.setData(bundle);
							handler.sendMessage(msg);*/
					}
				}
			};

			Thread thread = new Thread(runnable);
			thread.start();

		}






		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				String time = bundle.getString("t");
				tvTimer.setText(Html.fromHtml("<html><font color = '" + task.color + "'><small>" + task.nameTask + "</small><br></font>" + time + "</html>"));

			}
		};
	}
	private void startInvalidatingTracker() {

		Runnable runnable = new Runnable() {
			@Override public void run() {

				invalidateTrackerHandler.postDelayed(this, 10000);
				trackerView.invalidate();
			}
		};
		invalidateTrackerHandler.postDelayed(runnable, 10000);
	}

	@Override
	protected void onStop() {
		startHourTrackerSetting.value = trackerView.getHourStart();
		daoSettings.update(startHourTrackerSetting);
		super.onStop();
	}
}
