package com.mycompany.organaiser;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;

public class DayDealDialog extends DialogFragment {

	AdapterUpdateable updateable;
	TaskDayDeal deal;
	boolean isRedaction = false;

	@Override
	public void onAttach(Context context) {
		updateable = (AdapterUpdateable) context;
		super.onAttach(context);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new  AlertDialog.Builder(getContext());
		builder.setTitle("Create new deal");
		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_new_deal, null);
		
		final EditText etName = view.findViewById(R.id.et_dialog_create_new_deal_name);
		final TextView tvSettingColor = view.findViewById(R.id.tv_setting_color);
		final TextView tvColorBlue = view.findViewById(R.id.tv_color_blue);
		final TextView tvColorYellow = view.findViewById(R.id.tv_color_yellow);
		final TextView tvColorGreen = view.findViewById(R.id.tv_color_green);
		final TextView tvColorPurpl = view.findViewById(R.id.tv_color_purpl);
		final TextView tvColorRed = view.findViewById(R.id.tv_color_red);

		if(deal == null){
			deal = new TaskDayDeal("404");
		}else{
			isRedaction = true;
			etName.setText(deal.nameTask);
			tvSettingColor.setBackgroundColor(deal.color);
		}

		
		tvColorBlue.setOnClickListener( new View.OnClickListener(){
			@Override
			public void onClick(View v){
				tvSettingColor.setBackgroundResource(R.color.for_day_deal_1);
				deal.color = getResources().getColor(R.color.for_day_deal_1);
			}
		});
		
		tvColorYellow.setOnClickListener( new View.OnClickListener(){
				@Override
				public void onClick(View v){
					tvSettingColor.setBackgroundResource(R.color.for_day_deal_2);
					deal.color = getResources().getColor(R.color.for_day_deal_2);
				}
			});
			
		tvColorPurpl.setOnClickListener( new View.OnClickListener(){
				@Override
				public void onClick(View v){
					tvSettingColor.setBackgroundResource(R.color.for_day_deal_3);
					deal.color = getResources().getColor(R.color.for_day_deal_3);
				}
			});
		
		tvColorGreen.setOnClickListener( new View.OnClickListener(){
				@Override
				public void onClick(View v){
					tvSettingColor.setBackgroundResource(R.color.for_day_deal_4);
					deal.color = getResources().getColor(R.color.for_day_deal_4);
				}
			});
		
		tvColorRed.setOnClickListener( new View.OnClickListener(){
				@Override
				public void onClick(View v){
					tvSettingColor.setBackgroundResource(R.color.for_day_deal_5);
					deal.color = getResources().getColor(R.color.for_day_deal_5);
				}
			});
			
		builder.setView(view);
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface di, int i){
				di.cancel();
			}
		});
		
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface di, int i){
					MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(getContext());
					DaoTaskDayDeal daoDeal = new DaoTaskDayDeal(helper);
					deal.nameTask = etName.getText().toString();
					if(isRedaction){
						daoDeal.update(deal);
						updateable.updateAdapter(deal, isRedaction);
					}else{
						long id = daoDeal.add(deal);
						deal.id = id;
						updateable.updateAdapter(deal, isRedaction);
					}

				}
			});
		return builder.create();
	}

	public void setTaskDayDeal(TaskDayDeal deal){
		this.deal = deal;
	}
	
}
