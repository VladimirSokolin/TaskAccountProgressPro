package com.mycompany.organaiser;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.view.View.*;

import java.util.*;

public class TimeSpaceDialog extends DialogFragment {

	MyDatabaseOpenHelper dataHelper;
	DaoTaskDayDeal daoTaskDayDeal;
	DaoTask daoTask;
	TimeSpace timeSpace;
	boolean isChangeOld = false;
	public interface DTSNoter{
		void onResult(TimeSpace timeSpace, boolean isChangeOld);
	}

	public void setTimeSpace(TimeSpace timeSpace){
		this.timeSpace = timeSpace;
	}
	private DTSNoter noter;

	@Override
	public void onAttach(Context context){
		super.onAttach(context);
		noter = (DTSNoter) context;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Add new deal");

		dataHelper = new MyDatabaseOpenHelper(getContext());
		daoTaskDayDeal = new DaoTaskDayDeal(dataHelper);
		daoTask = new DaoTask(dataHelper);


		ArrayList<Task> listTasks = daoTask.getAllNoCompleteTask();
		listTasks.addAll(daoTaskDayDeal.getAll());

		MyGridAdapter adapter = new MyGridAdapter(getContext(), listTasks);

		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_new_time_space, null);
		final TextView tvFrom = view.findViewById(R.id.tv_from);
		final TextView tvTo = view.findViewById(R.id.tv_to);
		final EditText etNameDeal = view.findViewById(R.id.et_day_time_name_deal);
		final EditText etDescription = view.findViewById(R.id.et_time_space_description);
		final GridView gridView = view.findViewById(R.id.grid_view_dialog_add_new_timespace);

		if(timeSpace != null){
			etNameDeal.setText(timeSpace.nameDeal);
			etDescription.setText(timeSpace.description);
			tvFrom.setText(String.format("from  %s", timeSpace.timeStart));
			tvTo.setText(String.format("to %s", timeSpace.timeStop));
			isChangeOld = true;
		} else {
			timeSpace = new TimeSpace();
		}

		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> av, View v, int i, long l){
				etNameDeal.setText(((Task)av.getItemAtPosition(i)).nameTask);
				timeSpace.setColor(((Task)av.getItemAtPosition(i)).color);
			}

		});

		tvFrom.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view){

					Calendar calendar = Calendar.getInstance();
					int hour = calendar.get(Calendar.HOUR_OF_DAY);
					int minute = calendar.get(Calendar.MINUTE);

					TimePickerDialog timePicker = new TimePickerDialog((DayRoutingActivity) getActivity(),  new TimePickerDialog.OnTimeSetListener(){
							@Override
							public void onTimeSet(TimePicker tp, int h, int m){
								tvFrom.setText("from  " + h + ":" + m);
								Calendar calStart = Calendar.getInstance();
								calStart.set(Calendar.HOUR_OF_DAY, h);
								calStart.set(Calendar.MINUTE, m);
								timeSpace.setStartMlTime(calStart.getTimeInMillis());
							}
						}, hour, minute, true);

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
								Calendar calStop = Calendar.getInstance();
								calStop.set(Calendar.HOUR_OF_DAY, h);
								calStop.set(Calendar.MINUTE, m);
				
								timeSpace.setStopMlTime(calStop.getTimeInMillis());
							}
						}, hour, minute, true);

					timePicker.show();
				}
			});

		builder.setView(view);
		builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface di, int i){
				di.cancel();
			}
		});
		builder.setPositiveButton("Принять", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface di, int i){
				timeSpace.setNameDeal(etNameDeal.getText().toString());
				if(etDescription.getText().length() != 0){
					timeSpace.description = etDescription.getText().toString();
				}
				noter.onResult(timeSpace, isChangeOld);
			}
		});

		return builder.create();
	}	
}
