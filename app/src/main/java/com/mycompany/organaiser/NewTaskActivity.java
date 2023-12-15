package com.mycompany.organaiser;
import android.annotation.SuppressLint;
import android.app.*;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.*;
import android.widget.*;
import android.view.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

import java.text.*;

public class NewTaskActivity extends AppCompatActivity implements ColorListener {
	
	EditText etTitle;
	EditText etFullCount;
	Button btDateEnd;
	EditText etDescription;

	GridView gridView;
	DaoTask daoTask;
	Task task = new Task();
	int color;

	ArrayList<AbstractItemGridView> list;
	AbstractItemGridView item;
	UniversalAdapter adapter;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_task);


		androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_new_task_activity);
		setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();
		if(actionBar != null){
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		etTitle = findViewById(R.id.et_title);
		etFullCount = findViewById(R.id.et_full_count);
		btDateEnd = findViewById(R.id.et_date_end);
		etDescription = findViewById(R.id.et_description);
		gridView = findViewById(R.id.gv_colors_new_task_activity);
		
		daoTask = new DaoTask(new MyDatabaseOpenHelper(this));

		launchGridView();

		btDateEnd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// invoke date picker dialog
				Calendar calendar = Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog datePickerDialog = new DatePickerDialog(NewTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker datePicker, int year, int month, int day) {
						String dayStr = day < 10 ? "0" + day : "" + day;
						String monthStr = month < 10 ? "0" + month : "" + month;
						btDateEnd.setText(dayStr + "." + monthStr + "." + year);
					}
				}, year, month, day
				);
				datePickerDialog.show();

			}
		});

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AbstractItemGridView item = (AbstractItemGridView) parent.getItemAtPosition(position);
				if(item instanceof SettingItem){
					DialogChooseColor dialogChooseColor = new DialogChooseColor();
					dialogChooseColor.show(getSupportFragmentManager(), "dialogChooseColor");
				} else{
					color = item.getColor();
				}
			}
		});
	}
	
	public void clickBtAddTask(View view){

		task.nameTask = etTitle.getText().toString();
		task.fullCount = Integer.valueOf(etFullCount.getText().toString());
		if(btDateEnd != null) task.dateEndPlane = btDateEnd.getText().toString();
		task.description = etDescription.getText().toString();
		task.color = color;

		task.dateStart = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
		task.compute(System.currentTimeMillis());
		task.createRefNameOfCommit();

		boolean is = daoTask.addTask(task);

		if(is) {
			Toast.makeText(this, "succes", Toast.LENGTH_LONG);
			finish();
		}

	}

	private void launchGridView() {
		ViewCreater viewCreater = new ViewCreater() {
			@Override
			public View createView(int position, View convertView, ViewGroup parent, Object object) {
				AbstractItemGridView item = (AbstractItemGridView) object;


				View view = LayoutInflater.from(NewTaskActivity.this).inflate(R.layout.color_item, parent, false);
				ColorView colorView = view.findViewById(R.id.color_view);

				GradientDrawable drawable = new GradientDrawable();
				drawable.setShape(GradientDrawable.RECTANGLE);
				drawable.setCornerRadius(10);
				drawable.setColor(item.color);
				//set effect up one
				drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
				drawable.setGradientCenter(0.5f, 0.5f);


				colorView.setBackground(drawable);
				colorView.setElevation(20);
				colorView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);

				if(item.symbol != null) {
					colorView.setSymbol(item.symbol);
				}
				return view;
			}
		};
		item = new SettingItem("➕");
		AbstractItemGridView ci_1 = new ColorItem(Color.RED);
		AbstractItemGridView ci_2 = new ColorItem(Color.YELLOW);
		AbstractItemGridView ci_3 = new ColorItem(Color.GREEN);
		AbstractItemGridView ci_4 = new ColorItem(Color.BLUE);
		AbstractItemGridView ci_5 = new ColorItem(Color.CYAN);
		AbstractItemGridView ci_6 = new ColorItem(Color.MAGENTA);
		AbstractItemGridView ci_7 = new ColorItem(Color.GRAY);


		list = new ArrayList<>();
		list.add(ci_1);
		list.add(ci_2);
		list.add(ci_3);
		list.add(ci_4);
		list.add(ci_5);
		list.add(ci_6);
		list.add(ci_7);
		list.add(item);

		adapter = new UniversalAdapter(this, R.layout.color_item, list, viewCreater);
		gridView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_new_task_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if(item.getItemId() == R.id.ok_menu_new_task_activity){
			Toast.makeText(this, "save", Toast.LENGTH_LONG);
		}else if(item.getItemId() == android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onChooseColor(int color) {
		this.color = color;
		list.remove(item);
		ColorItem ci = new ColorItem(color);
		list.add(ci);
		list.add(item);
		adapter.notifyDataSetChanged();
	}
}
