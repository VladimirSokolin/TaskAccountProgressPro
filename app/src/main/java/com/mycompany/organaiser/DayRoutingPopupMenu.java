package com.mycompany.organaiser;
import android.widget.*;
import android.content.*;
import android.view.*;
import android.app.*;

public class DayRoutingPopupMenu extends PopupMenu implements DayRoutingPopupMenuInterface{
	
	private final String titleNew = "Установить новое расписание";
	private final String titleTemplate = "Выбрать шаблон расписания";
	private final String titleUpdate = "Редактировать текущее расписание";

	DialogConfirmationer confirmationer;
	long idCurrentDayRouting;
	
	public DayRoutingPopupMenu(final Context context, View view, final FragmentManager fm){
		super(context, view);
		confirmationer = new DialogConfirmationImpl(context);

		getMenu().add(titleNew);
		getMenu().add(titleTemplate);
		getMenu().add(titleUpdate);
		
		
		setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem menuItem){
				//Toast.makeText(context, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
				switch(menuItem.getTitle().toString()){
					case titleNew : {
						confirmationer.showDialogConfirmation(
							"Confirmation",
								"You want to delete current day shedule and create new?",
								"Ok",
								"Cancel",
								()->{
									Intent intent = new Intent(context, DayRoutingActivity.class);
									intent.putExtra("id", idCurrentDayRouting);
									intent.putExtra("isNew", true);
									context.startActivity(intent);
								}
						);
					}
					break;
					case titleTemplate : {
						DialogSetDayRoutingTemplate dialog = new DialogSetDayRoutingTemplate();
						dialog.show(fm, "template");
					}
					break;
					case titleUpdate : {
						Intent intent = new Intent(context, DayRoutingActivity.class);
						intent.putExtra("id", idCurrentDayRouting);
						intent.putExtra("isNew", false);
						context.startActivity(intent);
					}
				}
				return false;
			}
		});
	}

	@Override
	public void showPopupMenuOfDayRoutingSet(){
		show();
	}

	public void setIdCurrentDayRouting(long id){
		idCurrentDayRouting = id;
	}

}
