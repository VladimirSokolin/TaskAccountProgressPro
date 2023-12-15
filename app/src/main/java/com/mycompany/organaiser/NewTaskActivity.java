package com.mycompany.organaiser;

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
	TextView tvDateEnd;
	TextView tvSetColor;
	EditText etDescription;
	androidx.appcompat.widget.Toolbar toolbar;

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


		toolbar = findViewById(R.id.toolbar_new_task_activity);
		setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();
		if(actionBar != null){
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		Window window = getWindow();
		window.getDecorView().setBackgroundColor(getResources().getColor(R.color.item_task_main));
		window.setStatusBarColor(getResources().getColor(R.color.item_task_main));
		window.setNavigationBarColor(getResources().getColor(R.color.item_task_main));

		
		etTitle = findViewById(R.id.et_title);
		etFullCount = findViewById(R.id.et_full_count);
		tvDateEnd = findViewById(R.id.tv_new_task_set_date_plane);
		tvSetColor = findViewById(R.id.tv_new_task_set_color);
		etDescription = findViewById(R.id.et_description);
		gridView = findViewById(R.id.gv_colors_new_task_activity);
		
		daoTask = new DaoTask(new MyDatabaseOpenHelper(this));

		tvDateEnd.setOnClickListener(new View.OnClickListener() {
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
						int rawMonth = month;
						month++;
						if(month == 13) {
							month = 1;
						}
						String dayStr = day < 10 ? "0" + day : "" + day;
						String monthStr = month < 10 ? "0" + month : "" + month;
						tvDateEnd.setText(dayStr + "." + monthStr + "." + year);
					}
				}, year, month, day
				);
				datePickerDialog.show();
			}
		});

		tvSetColor.setOnClickListener((view) -> {
			PRDialog prDialog = new PRDialog(NewTaskActivity.this, R.layout.dialog_new_task_color);
			prDialog.setPreparer((layout) -> {

				LinearLayout linear = (LinearLayout) layout.findViewById(R.id.layout_dialog_fast_note);
				if(linear != null) {
					GradientDrawable gradientDrawable = new GradientDrawable();
					gradientDrawable.setColor(this.getResources().getColor(R.color.item_task_main));
					gradientDrawable.setCornerRadius(30);
					linear.setBackground(gradientDrawable);
				}

				GradientDrawable drawable = new GradientDrawable();
				drawable.setShape(GradientDrawable.RECTANGLE);
				drawable.setCornerRadius(20);


				Button btOk = layout.findViewById(R.id.bt_ok_dialog_new_task_color);
				Button btCancel = layout.findViewById(R.id.bt_cancel_dialog_new_task_color);
				TextView tvCurrentColor = layout.findViewById(R.id.tv_dialog_new_task_color);
				tvCurrentColor.setBackgroundColor(Color.rgb(145, 49, 81));
				GridView gridView = layout.findViewById(R.id.grid_view_dialog_new_task_color);
				launchGridView(gridView);
				gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						AbstractItemGridView item = (AbstractItemGridView) parent.getItemAtPosition(position);
						if(item instanceof SettingItem){
							DialogChooseColor dialogChooseColor = new DialogChooseColor();
							dialogChooseColor.show(getSupportFragmentManager(), "dialogChooseColor");
						} else{
							setColor(item.getColor());
							drawable.setColor(item.getColor());
							tvCurrentColor.setBackgroundDrawable(drawable);
						}
					}
				});
				btOk.setOnClickListener((v) -> {
					drawable.setColor(color);
					tvSetColor.setBackgroundDrawable(drawable);
					prDialog.dismiss();
				});
				btCancel.setOnClickListener((v) -> {
					prDialog.cancel();
				});
			});
			prDialog.showDialog();
		});


	}

	//listener on menu click
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
	    int id = item.getItemId();
	    if (id == R.id.ok_menu_new_task_activity) {
	        // Add your code here for the menu item click listener
			if(etTitle.getText().toString().isEmpty()) {
				etTitle.setError("Enter title!");
				Toast.makeText(this, "Enter title!", Toast.LENGTH_LONG).show();
				return false;
			} else {
				task.nameTask = etTitle.getText().toString();
			}
			if(etFullCount.getText().toString().isEmpty()) {
				task.fullCount = 0;
			} else {
				task.fullCount = Integer.valueOf(etFullCount.getText().toString());
			}
			if(tvDateEnd != null) task.dateEndPlane = tvDateEnd.getText().toString();
			task.description = etDescription.getText().toString();
			task.color = color;

			task.dateStart = new SimpleDateFormat("dd.MM.yyyy" , Locale.getDefault()).format(new Date());
			task.compute(System.currentTimeMillis());
			task.createRefNameOfCommit();
			boolean is = daoTask.addTask(task);
			if(is) {
				Toast.makeText(this, "Task saved successfully!", Toast.LENGTH_LONG).show();
				finish();
			}
	        return true;
	    }
		if(id == android.R.id.home){
			finish();
		}
	    return super.onOptionsItemSelected(item);
	}
	private void launchGridView(GridView gridView) {
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
				colorView.setElevation(10);
				colorView.setTranslationZ(5);
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
	public void onChooseColor(int color) {
		this.color = color;
		list.remove(item);
		ColorItem ci = new ColorItem(color);
		list.add(ci);
		list.add(item);
		adapter.notifyDataSetChanged();
	}

	public void setColor(int color){
		this.color = color;
	}
}
