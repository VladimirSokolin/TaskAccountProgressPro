package com.mycompany.organaiser;
import android.content.*;
import android.widget.*;
import android.graphics.drawable.*;

public class RoundedTextView extends TextView {
	public RoundedTextView(Context context){
		super(context);
		setRoundedBackground();
	}
	
	private void setRoundedBackground(){
		GradientDrawable gd = new GradientDrawable();
		gd.setShape(GradientDrawable.RECTANGLE);
		gd.setCornerRadius(20);
		gd.setColor(getResources().getColor(android.R.color.holo_blue_light));
		setBackground(gd);
		
	}
}
