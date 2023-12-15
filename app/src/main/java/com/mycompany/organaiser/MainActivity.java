package com.mycompany.organaiser;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.*;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.widget.*;
import android.view.*;
import java.util.*;
import android.content.*;
import android.widget.AdapterView.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarItemView;

import java.text.*;

public class MainActivity extends AppCompatActivity {
	TextView tvSecond;
	Button btAddFastNote;
	Button btMenu;
	Button btTracker;
	Button btNotePad;
	Button btSettings;
	OrganaiserDao daoCommit;
	ArrayList<Task> list;
	ArrayList<FastNote> listFastNotes;
	Commit commit;
	SetOfEntities setEntities;
	MyDatabaseOpenHelper dataHelper;
	DaoTask daoTask;
	DaoFastNote daoFastNote;
	ListAdapter mAdapter;
	AdapterFastNote adapterFastNote;
	ListView mListView;
	RecyclerView recyclerViewFastNotes;
	DialogFastNoteCreate dialogFastNoteCreate;
	FastNoteManager fastNoteManager;
	TextView tvImportantInfo;

	
	int x = 0;
	
	
    @Override
    protected void onCreate(Bundle 	savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.item_task_main));
		getWindow().setStatusBarColor(getResources().getColor(R.color.item_task_main));
		getWindow().setNavigationBarColor(getResources().getColor(R.color.item_task_main));



		Toolbar toolbar = findViewById(R.id.toolbar_main_activity);

		setSupportActionBar(toolbar);

		// Change color of bottom navigation bar
		Date yesterdayDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
		SimpleDateFormat sdfDayOfWeek = new SimpleDateFormat("EEEE", Locale.getDefault());
		String currentDay = sdf.format(yesterdayDate);
		
		dataHelper = new MyDatabaseOpenHelper(this);
		
		daoCommit = new DaoOrganaiserLearn(dataHelper);
		daoTask = new DaoTask(dataHelper);
		daoFastNote = new DaoFastNote(dataHelper);

		mListView = (ListView) findViewById(R.id.list_view_task);
		recyclerViewFastNotes = findViewById(R.id.recycler_view_fast_notes);
		fastNoteManager = new FastNoteManager(dataHelper);
		tvImportantInfo = findViewById(R.id.tv_important_information);
		tvImportantInfo.setMovementMethod(new ScrollingMovementMethod());
		tvImportantInfo.setText(Html.fromHtml("<big>Today: " + currentDay + " - " + sdfDayOfWeek.format(yesterdayDate)+ "</big>"));

		btMenu = findViewById(R.id.bt_navigation_menu);
		Drawable drawableNavigateFocusButton = getDrawable(R.drawable.shape_view_navigate_focus);
		drawableNavigateFocusButton.setColorFilter(getResources().getColor(R.color.item_task_main), android.graphics.PorterDuff.Mode.SRC_IN);
		btMenu.setBackground(drawableNavigateFocusButton);
		btMenu.setTranslationY(-15);

		btTracker = findViewById(R.id.bt_navigation_tracker);
		btTracker.setOnClickListener((view)->{
			startActivity(new Intent(this, CommitActivity.class));
		});
		btNotePad = findViewById(R.id.bt_navigation_notePad);

		btSettings = findViewById(R.id.bt_navigation_settings);

		listFastNotes = fastNoteManager.getAllCurrentDate();
		adapterFastNote = new AdapterFastNote(listFastNotes);
		recyclerViewFastNotes.setAdapter(adapterFastNote);


		btAddFastNote = findViewById(R.id.bt_add_fast_notes);

		LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		recyclerViewFastNotes.setLayoutManager(layoutManager);
		//set click listener on item of recycler
		adapterFastNote.setOnClickNoteListener ( fn -> {
			DealPopupMenu popupMenu = new DealPopupMenu(this, recyclerViewFastNotes, getFragmentManager());
			popupMenu.setEditDealListener(()->{
				DialogFastNoteCreate dialog = new DialogFastNoteCreate(this, fastNoteManager, fn);
				dialog.setOnUpdateListener( (object, isRedact)->{
					Thread thread = new Thread(()->{
						ArrayList<FastNote> currentListAfterEdit = fastNoteManager.getAllCurrentDate();
						listFastNotes.clear();
						listFastNotes.addAll(currentListAfterEdit);
					});
					thread.start();
					synchronized (thread) {
						adapterFastNote.notifyDataSetChanged();
					}

				});
				dialog.showDialog();
			});
			popupMenu.setDeleteDealListener(()->{
				fastNoteManager.delete(fn.id);
				listFastNotes.remove(fn);
				adapterFastNote.notifyDataSetChanged();
			});
			popupMenu.show();
		});

		btAddFastNote.setOnClickListener(view -> {
			dialogFastNoteCreate = new DialogFastNoteCreate(this, fastNoteManager);
			dialogFastNoteCreate.setOnUpdateListener( (object, isRedact)->{
				if(!isRedact) {
					ArrayList<FastNote> currentListAfterEdit = fastNoteManager.getAllCurrentDate();
					if(listFastNotes.size()<currentListAfterEdit.size()){
						listFastNotes.clear();
						listFastNotes.addAll(currentListAfterEdit);
					}
					adapterFastNote.notifyDataSetChanged();
				}
			});
			dialogFastNoteCreate.showDialog();
		});
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> av, View view, int i, long l){
				Intent intent = new Intent(MainActivity.this, CommitActivity.class);
				intent.putExtra("id", list.get(i).id);
				startActivity(intent);
				return false;
			}
		});
    }

	class MyAdapter extends ArrayAdapter<Task> {
		public MyAdapter(Context context, ArrayList arrayTasks){
			super(context, R.layout.task_item, arrayTasks);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View view = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
			Task curTask = getItem(position);

			String title = curTask.nameTask;
			String currentFullCountString = "Current count: "+curTask.currentCount + " / " + curTask.fullCount ;
			
			TextView textTitle = (TextView) view.findViewById(R.id.tv_name_task_main_activity);
			textTitle.setText(title);
			textTitle.setClipToOutline(true);
		 	TextView textComments = (TextView) view.findViewById(R.id.tv_comments_main_activity);
			textComments.setClipToOutline(true);

			if(curTask.countInDay == 0){
				textComments.setText(currentFullCountString + "\nThe daily actual rate is calculated - not enough time has passed");
			} else {
				textComments.setText(currentFullCountString + "\n" + curTask.dayToComlete + " days left until completion, normal: " + curTask.countInDay + " per day");
			}
			ProgressTaskView progressTaskView = view.findViewById(R.id.progress_task_view_main_activity);
			progressTaskView.setCount(curTask.currentCount, curTask.fullCount);

			
			return view;
		}
		
		
		
	}
	
	private void update(){
		list = daoTask.getAllTask();
		mAdapter = new MyAdapter(MainActivity.this, list);
		mListView.setAdapter(mAdapter);
	}

	@Override
	protected void onStart() {
		update();
		super.onStart();
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_main_activity, menu);
		/*for (int i = 0; i < menu.size(); i++) {
			MenuItem item = menu.getItem(i);
			SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
			spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.item_task_main)), 0, spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			item.setTitle(spanString);
		}*/

		return true;
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		if (id == R.id.item_add_task) {
			startActivity(new Intent(MainActivity.this, NewTaskActivity.class));
			return true;
		} else if (id == R.id.item_achievements) {
			startActivity(new Intent(MainActivity.this, ProgressActivity.class));
			return true;
		} else if (id == R.id.item_commit) {
			startActivity(new Intent(MainActivity.this, CommitActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	
}
