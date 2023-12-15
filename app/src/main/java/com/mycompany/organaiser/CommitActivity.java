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


public class CommitActivity extends Activity implements CommitCompleteInterface, DialogSetDayRoutingTemplate.SetRoutingDialogListener, AdapterUpdateable {
	
	String currentDay;
	ListAdapter mListAdapter;
	GridView mGridView;
	DaoTask daoTask;
	DaoTaskDayDeal daoDeal;
	DaoOrganaiserLearn daoCommit;
	DaoTimeSpace daoTimeSpace;
	DaoDayRouting daoDayRouting;
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

	TimeSpaceCommiter commiter;
	DialogConfirmationer confirmationer;
	boolean clickTaskDealForRedact = false;

	Timekeeper timekeeper;
	long startTime = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState){	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commit);
		
		getActionBar().hide();

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

		currentDayRouting = setCurrentDayRouting();
		
		tvTimer = findViewById(R.id.tv_timer);
		
		tvFactNote = findViewById(R.id.tv_fact_note);
		tvFactNote.setClipToOutline(true);
		tvFactNote.setMovementMethod(new ScrollingMovementMethod());
		
		tvResume = findViewById(R.id.tv_resume);
		tvResume.setClipToOutline(true);
		tvResume.setMovementMethod(new ScrollingMovementMethod());

		btTracker = findViewById(R.id.bt_navigation_tracker);
		Drawable drawableNavigateFocusButton = getDrawable(R.drawable.shape_view_navigate_focus);
		drawableNavigateFocusButton.setColorFilter(getResources().getColor(R.color.item_task_main), android.graphics.PorterDuff.Mode.SRC_IN);
		btTracker.setBackground(drawableNavigateFocusButton);
		btTracker.setTranslationY(-15);

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


				} else {
					Toast.makeText(CommitActivity.this, "The accounting task is edited in the main menu", Toast.LENGTH_LONG).show();
				}
				return false;
			}
		});
		
		
		mGridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> l, View view, int position, long id){
				if(!clickTaskDealForRedact) {
					if(!isPress){
						startTime = System.currentTimeMillis();
						task = (Task) l.getItemAtPosition(position);
					}
					startProcessAccounting(task, startTime, view);
				}
				clickTaskDealForRedact = false;
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
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(isPress){
			timekeeper.isInProcess = true;
			if(task instanceof TaskDayDeal){
				timekeeper.typeTask = 2;
			} else {
				timekeeper.typeTask = 1;
			}
			timekeeper.timeStart = startTime;
			timekeeper.idTask = task.id;
			timekeeper.dao.updateTimekeeper(timekeeper);
		}
	}

	private void update(){
		listTasks = daoTask.getAllTask();
		listDeals = daoDeal.getAll();
		
		for(TaskDayDeal deal : listDeals){
			listTasks.add(deal);
		}
		
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
	
	void showDialogProba() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//builder.setTitle("proba");
		//builder.setMessage("interest what to do");

		final View mLayout = getLayoutInflater().inflate(R.layout.dialog_complete_commit, null);
		TextView startPage = mLayout.findViewById(R.id.dialog_tv_start_page);
		startPage.setText("last time we stopped at: " + task.currentCount);
		final EditText etPage = mLayout.findViewById(R.id.et_new_current_page);
		final Spinner etAttention = mLayout.findViewById(R.id.spinner_attention);

		/*builder.setPositiveButton("Ok", new  DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface di, int i){

				String strPage = etPage.getText().toString();
				String strAttention = etAttention.getSelectedItem().toString();
				// get array from resources array_string
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

				double dPage = Double.parseDouble(strPage);
			 	String dDescription = ((EditText)mLayout.findViewById(R.id.description_dialog_commit)).getText().toString();

				onCommitComplete(dPage, dDescription, dAttention);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface di, int i){
				di.cancel();
			}
		});*/
		AlertDialog alertDialog = builder.create();

		Window w = alertDialog.getWindow();
		if (w != null) {
			w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		}
		alertDialog.setCanceledOnTouchOutside(false);

		alertDialog.show();

		/*Button btPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
		if(btPositive != null){
			btPositive.setBackgroundColor(getResources().getColor(R.color.item_task_main));
			btPositive.setTextColor(Color.WHITE);
		} else Log.i("buttons", "bt null");

		Button btNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
		if(btNegative != null){
			btNegative.setBackgroundColor(getResources().getColor(R.color.item_task_main));
			btNegative.setTextColor(Color.WHITE);
		} else Log.i("buttons", "bt null");*/

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
			tvResume.setText("");
			commiter.rangeAndCommitAllTimeSpaces(currentDayRouting.listOfTimeSpaces, tvResume);
		}

	}
	
	void loadAndSetFactTimesCurrentDay(String currentDay){
		
		ArrayList<TimeSpace> list = daoTimeSpace.getAllTimeSpaceOfDayIsFact(currentDay);
		for(TimeSpace ts : list){
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
			listTasks.add((Task)task);
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

				ObjectAnimator scaleTextSize = ObjectAnimator.ofFloat(tvTimer, "textSize", 16f);
				scaleTextSize.setDuration(1000); // Продолжительность анимации в миллисекундах
				scaleTextSize.start();
			}

		} else {

			task.compute(startTime); // первый подсчет
			tvTimer.setText(task.nameTask);
			if(view != null) {
				view.setBackgroundColor(getResources().getColor(android.R.color.tertiary_text_light));

				ObjectAnimator scaleTextSize = ObjectAnimator.ofFloat(tvTimer, "textSize", 24f);
				scaleTextSize.setDuration(1000); // Продолжительность анимации в миллисекундах
				scaleTextSize.start();
			}


			if (!(task instanceof TaskDayDeal)) {
				commit = new Commit((int) task.currentCount, startTime);
			}
			isPress = true;

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
}
