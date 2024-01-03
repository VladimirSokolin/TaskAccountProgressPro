package com.mycompany.organaiser;

import android.app.*;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.*;
import android.widget.*;
import android.view.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

import java.text.*;

import yuku.ambilwarna.AmbilWarnaDialog;

public class NewTaskActivity extends AppCompatActivity implements ColorListener {
	
	EditText etTitle;
	EditText etFullCount;
	TextView tvDateEnd;
	TextView tvSetColor;
	EditText etDescription;
	androidx.appcompat.widget.Toolbar toolbar;

	GridView gridView;
	DaoTask daoTask;
	DaoSettings daoSettings;
	Task task;
	int color;
	boolean isEdit = false;

	ArrayList<AbstractItemGridView> list;
	AbstractItemGridView item;
	UniversalAdapter adapter;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_task);

		MyDatabaseOpenHelper dataHelper = new MyDatabaseOpenHelper(this);
		daoSettings = new DaoSettings(dataHelper);
		int colorScreen = daoSettings.getByTitle("color").value;

		toolbar = findViewById(R.id.toolbar_new_task_activity);
		setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();
		if(actionBar != null){
			actionBar.setBackgroundDrawable(new ColorDrawable(colorScreen));
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		Window window = getWindow();
		window.getDecorView().setBackgroundColor(colorScreen);
		window.setStatusBarColor(colorScreen);
		window.setNavigationBarColor(colorScreen);



		
		etTitle = findViewById(R.id.et_title);
		etFullCount = findViewById(R.id.et_full_count);
		tvDateEnd = findViewById(R.id.tv_new_task_set_date_plane);
		tvSetColor = findViewById(R.id.tv_new_task_set_color);
		etDescription = findViewById(R.id.et_description);
		gridView = findViewById(R.id.gv_colors_new_task_activity);

		daoTask = new DaoTask(dataHelper);

		Intent intent = getIntent();
		long id = intent.getLongExtra("id", 0);
		if(id != 0) {
			task = daoTask.getTask(id);
			etTitle.setText(task.nameTask);
			etFullCount.setText(String.valueOf(task.fullCount));
			if(task.dateEndPlane != null) tvDateEnd.setText(task.dateEndPlane);
			if(task.description != null) etDescription.setText(task.description);
			if(task.color != 0) {
				color = task.color;
				GradientDrawable drawable = new GradientDrawable();
				drawable.setShape(GradientDrawable.RECTANGLE);
				drawable.setCornerRadius(20);
				drawable.setColor(task.color);
				tvSetColor.setBackground(drawable);
			}
			launchGridViewOldTaskItem(gridView);
			isEdit = true;

		} else {
			task = new Task();
		}
		


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
					gradientDrawable.setColor(colorScreen);
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
							AmbilWarnaDialog warnaDialog = new AmbilWarnaDialog(NewTaskActivity.this, 0xff0000ff, new AmbilWarnaDialog.OnAmbilWarnaListener() {
								@Override
								public void onCancel(AmbilWarnaDialog dialog) {
									dialog.getDialog().cancel();
								}
								@Override
								public void onOk(AmbilWarnaDialog dialog, int color) {
									setColor(color);
									drawable.setColor(color);
									tvCurrentColor.setBackgroundDrawable(drawable);
									list.remove(item);
									ColorItem ci = new ColorItem(color);
									list.add(ci);
									list.add(item);
									adapter.notifyDataSetChanged();
								}
							});
							warnaDialog.show();
							/*DialogChooseColor dialogChooseColor = new DialogChooseColor();
							dialogChooseColor.show(getSupportFragmentManager(), "dialogChooseColor");*/
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
				task.fullCount = Double.valueOf(etFullCount.getText().toString());
			}
			if(tvDateEnd != null) task.dateEndPlane = tvDateEnd.getText().toString();
			task.description = etDescription.getText().toString();
			if(color != 0){
				task.color = color;
			} else task.color = Color.GRAY;


			if(isEdit){
				task.computeBeforeUpdate();
				daoTask.update(task);
				Toast.makeText(this, "Task updated successfully!", Toast.LENGTH_LONG).show();
			} else {
				task.dateStart = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
				task.compute(System.currentTimeMillis());

				boolean is = daoTask.addTask(task);
				if (is) {
					Toast.makeText(this, "Task saved successfully!", Toast.LENGTH_LONG).show();
					finish();
				}
			}
	        return true;
	    }
		if(id == android.R.id.home){
			finish();
		}

		if(id == R.id.delete_menu_new_task_activity){
			if(task != null && isEdit){
				daoTask.delete(task.id);
				finish();
			} else {
				Toast.makeText(this, "The task must first be set", Toast.LENGTH_LONG).show();
			}
		}

		if(id == R.id.perform_menu_new_task_activity){
			if(task != null && isEdit){
				task.isComplete = true;
				daoTask.update(task);
				Toast.makeText(this, "Congratulations! we are performed this task", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "The task must first be set", Toast.LENGTH_LONG).show();
			}
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

				drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
				drawable.setGradientCenter(0.5f, 0.5f);

				colorView.setBackground(drawable);
				colorView.setElevation(20);
				colorView.setTranslationZ(10);
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
	private void launchGridViewOldTaskItem(GridView gridView) {
		ViewCreater viewCreater = new ViewCreater() {
			@Override
			public View createView(int position, View convertView, ViewGroup parent, Object object) {
				SummaryOfTask item = (SummaryOfTask) object;
				View view = LayoutInflater.from(NewTaskActivity.this).inflate(R.layout.item_settings_old_task, parent, false);
				ImageView iv = view.findViewById(R.id.iv_task_statistics);
				TextView tv = view.findViewById(R.id.tv_task_statistics);
				iv.setImageBitmap(item.bitmap);
				tv.setText(item.name);
				return view;
				//Bitmap bitmap = Bitmap.createBitmap(new int[]{R.drawable.commit_view}, 20, 20, Bitmap.Config.ARGB_8888);
			}
		};

		ArrayList<SummaryOfTask> listOfSummary = new ArrayList<>();
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.commit_view);
		listOfSummary.add(new SummaryOfTask("Statistics", bitmap));
		listOfSummary.add(new SummaryOfTask("Graphics", bitmap));
		listOfSummary.add(new SummaryOfTask("Comments", bitmap));

		UniversalAdapter<SummaryOfTask> adapter = new UniversalAdapter<>(this, R.layout.item_settings_old_task, listOfSummary, viewCreater);
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
