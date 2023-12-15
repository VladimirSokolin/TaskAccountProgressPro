package com.mycompany.organaiser;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import java.util.*;
import android.content.*;

public class DayRoutingActivity extends Activity implements TimeSpaceDialog.DTSNoter {

	ImageButton ibAdd;
	ListView lvTimeSpaces;
	DayRouting dayRouting;
	DaoTimeSpace daoTimeSpace;
	DaoDayRouting daoDayRouting;
	MyDatabaseOpenHelper dataHelper;
	long idSendedDayRouting;
	boolean isSetNewDayRouting = false;
	ItemTSAdapter adapter;

	TimeSpaceCommiter commiter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_routing);

		getActionBar().hide();

		commiter = new TimeSpaceCommiterImpl();
		ibAdd = findViewById(R.id.fab_day_routing_add);
		lvTimeSpaces = findViewById(R.id.list_view_time_space_dayrouting);
		
		
		dataHelper = new MyDatabaseOpenHelper(this);
		daoTimeSpace = new DaoTimeSpace(dataHelper);
		daoDayRouting = new DaoDayRouting(dataHelper);

		Intent intent = getIntent();
		idSendedDayRouting = intent.getLongExtra("id", -1);
		isSetNewDayRouting = intent.getBooleanExtra("isNew", false);

		dayRouting = setDayRouting(idSendedDayRouting);
		Toolbar toolbar = findViewById(R.id.toolbarDayRoutingActivity);

		lvTimeSpaces.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				TimeSpace timeSpace = (TimeSpace) parent.getItemAtPosition(position);
				TimeSpaceDialog timeSpaceDialog = new TimeSpaceDialog();
				timeSpaceDialog.setTimeSpace(timeSpace);
				timeSpaceDialog.show(getFragmentManager(), "TimeSpaceDialog");
				return false;
			}
		});

		
		toolbar.findViewById(R.id.save_day_routing_activity).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(dayRouting.isTamplate()){
					dayRouting.id = daoDayRouting.add(dayRouting);
					for(TimeSpace ts : dayRouting.listOfTimeSpaces){
						daoTimeSpace.addTimeSpace(ts, dayRouting.id);
					}
					Toast.makeText(DayRoutingActivity.this, "Template saved!", Toast.LENGTH_SHORT).show();
				}else {
					daoTimeSpace.deleteTimeSpacesOfDayRoutingIsPlane(dayRouting.id);
					for (TimeSpace ts : dayRouting.listOfTimeSpaces) {
						daoTimeSpace.addTimeSpace(ts, dayRouting.id);
					}
					Toast.makeText(DayRoutingActivity.this, "New shedule saved!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		toolbar.findViewById(R.id.comeback_day_routing_activity).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				onBackPressed();
			}
		});
		toolbar.findViewById(R.id.to_do_template).setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					if(!dayRouting.isTamplate()){
						AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DayRoutingActivity.this);
						dialogBuilder.setTitle("Set Template Name");

						final EditText etTemplateName = new EditText(DayRoutingActivity.this);
						etTemplateName.setHint("Write template name");
						dialogBuilder.setView(etTemplateName);

						dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						    @Override
						    public void onClick(DialogInterface dialog, int which) {
						        String templateName = etTemplateName.getText().toString();
						        dayRouting.toDoTemplate(templateName);
								v.setBackground(getDrawable(R.drawable.red_star));
						        // TODO: Handle template name
						    }
						});

						dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						    @Override
						    public void onClick(DialogInterface dialog, int which) {
						        dialog.dismiss();
						    }
						});

						AlertDialog dialog = dialogBuilder.create();
						dialog.show();

					} else {
						dayRouting.setTamplate(false);
						v.setBackground(getDrawable(R.drawable.white_star));
					}
				}
			});
		
		
		
		
		ibAdd.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				TimeSpaceDialog dts = new TimeSpaceDialog();
				dts.show(getFragmentManager(), "timer");
			}
		});
	}

	@Override
	protected void onResume() {
		setTimeSpacesOfDayRouting();
		super.onResume();
	}
	
	

	@Override
	public void onResult(TimeSpace timeSpace, boolean isChanged) {
		if(isChanged){
			sortListOfTimeSpaces();
			/*if(adapter == null){
				setupListView();
			}else*/ adapter.notifyDataSetChanged();
		}else{
			timeSpace.date = dayRouting.date;
			dayRouting.addTimeSpace(timeSpace);
			sortListOfTimeSpaces();
			// display new TimeSpace on screen in lvTimeSpaces
			if(adapter == null){
				setupListView();
			}else adapter.notifyDataSetChanged();
		}


	}

	private ArrayList<TimeSpace> getTimeSpaces(long ForeignKey){
		if(daoTimeSpace != null){
			ArrayList<TimeSpace> listTimeSpaces = daoTimeSpace.getAllTimeSpaceOfDayIsPlane(ForeignKey);
			return listTimeSpaces;
		}
		return null;
	}

	private void setTimeSpacesOfDayRouting(){

		if(!isSetNewDayRouting){
			dayRouting.listOfTimeSpaces = getTimeSpaces(dayRouting.id);
			if(dayRouting.listOfTimeSpaces != null){
				setupListView();
			}

		}
	}

	private DayRouting setDayRouting(long id){
		if(daoDayRouting != null) {
			if(id != -1){
				DayRouting dayRouting = daoDayRouting.getDayRouting(id);
				return dayRouting;
			}else {
				Toast.makeText(this, "Error in getting daily schedule ", Toast.LENGTH_SHORT).show();
			}

		}
		return null;
	}

	private void setupListView(){
		adapter = new ItemTSAdapter(this, dayRouting.listOfTimeSpaces, (ts)->{
			// code executed when user click on image_view_delete
			//daoTimeSpace.delete(ts.id); all changes in database must be only after click on toolbar save
			dayRouting.listOfTimeSpaces.remove(ts);
		});
		lvTimeSpaces.setAdapter(adapter);
	}

	private void sortListOfTimeSpaces(){
		dayRouting.listOfTimeSpaces.sort((o1, o2) -> {
			if(o1.timeMlStart > o2.timeMlStart) return 1;
			else if(o2.timeMlStart > o1.timeMlStart) return -1;
			else return 0;
		});
	}
	
	/*public static class DialogTimeSpace extends DialogFragment {

	
		public interface DialogTimeSpaceNoter{
			void onResult(String from, String to);
		}
		
		private DialogTimeSpaceNoter noter;
		
		@Override
		public void onAttach(Context context){
			super.onAttach(context);
			noter = (DialogTimeSpaceNoter) context;
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Add new deal");
			
			View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_new_time_space, null);
			final TextView tvFrom = view.findViewById(R.id.tv_from);
			final TextView tvTo = view.findViewById(R.id.tv_to);
			
			tvFrom.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view){
					//Toast.makeText((DayRoutingActivity)getActivity(), "from", Toast.LENGTH_SHORT).show();
					
					Calendar calendar = Calendar.getInstance();
					int hour = calendar.get(Calendar.HOUR_OF_DAY);
					int minute = calendar.get(Calendar.MINUTE);
					
					TimePickerDialog timePicker = new TimePickerDialog((DayRoutingActivity) getActivity(),  new TimePickerDialog.OnTimeSetListener(){
						@Override
						public void onTimeSet(TimePicker tp, int h, int m){
							tvFrom.setText("from  " + h + ":" + m);
						}
					}, hour, minute, false);
					
					timePicker.show();
				}
			});
			
			tvTo.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view){
					//Toast.makeText((DayRoutingActivity)getActivity(), "to", Toast.LENGTH_SHORT).show();
					
					Calendar calendar = Calendar.getInstance();
					int hour = calendar.get(Calendar.HOUR_OF_DAY);
					int minute = calendar.get(Calendar.MINUTE);

					TimePickerDialog timePicker = new TimePickerDialog((DayRoutingActivity) getActivity(),  new TimePickerDialog.OnTimeSetListener(){
							@Override
							public void onTimeSet(TimePicker tp, int h, int m){
								tvTo.setText("to  " + h + ":" + m);
							}
						}, hour, minute, false);

					timePicker.show();
				}
			});
			
			builder.setView(view);
			
			return builder.create();
		}	
	}*/
	
	//public static class DialogTimePicker extends TimePickerDialog{
		
		
		
}
