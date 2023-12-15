package com.mycompany.organaiser;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.widget.AdapterView.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DialogSetDayRoutingTemplate extends DialogFragment {
	
	
	interface SetRoutingDialogListener{
		void setRouting(DayRouting dayRouting);
	}
	
	SetRoutingDialogListener listener;

	@Override
	public void onAttach(Context context){
		
		listener = (CommitActivity) context;
		super.onAttach(context);
	}


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("Pick template");
		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_set_template_routing, null);
		
		
		ArrayList<DayRouting> listTemplate = new DaoDayRouting(new MyDatabaseOpenHelper(getContext())).getTemplates();
		
		ListView listView = view.findViewById(R.id.list_dialog_set_template);
		ListAdapter adapter = new MyAdapter(getContext(), listTemplate);
		listView.setAdapter(adapter);
	
		builder.setView(view);
		
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView av, View v, int i, long l){
				listener.setRouting((DayRouting)av.getItemAtPosition(i));
				dismiss();
			}
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface di, int i){
				di.cancel();
			}
		});
		return builder.create();
	}


	private class MyAdapter extends ArrayAdapter<DayRouting> {
		public MyAdapter(Context context, ArrayList<DayRouting> arrayTasks){
			super(context, R.layout.entity, arrayTasks);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = LayoutInflater.from(getContext()).inflate(R.layout.entity, parent, false);
			DayRouting day = getItem(position);
			TextView textTitle = (TextView) view.findViewById(R.id.entityTextView1);
			String date = day.date;

			// Use regular expressions to remove the first word "template " from the sentence
			Pattern pattern = Pattern.compile("^template\\s(.*)");
			Matcher matcher = pattern.matcher(date);
			if (matcher.find()) {
				date = matcher.group(1);
			}

			textTitle.setText(date);
			return view;
		}
	}


}

