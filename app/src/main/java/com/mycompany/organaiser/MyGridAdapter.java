package com.mycompany.organaiser;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class MyGridAdapter extends ArrayAdapter<Task> {
    Context mContext;
    GradientDrawable drawable;
    public MyGridAdapter(Context context, ArrayList<Task> list){
        super(context, R.layout.entity, list);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.entity, parent, false);

        Task task = (Task)getItem(position);
        TextView rtv = view.findViewById(R.id.entityTextView1);

        rtv.setElevation(20);
        rtv.setOutlineProvider(ViewOutlineProvider.BACKGROUND);

        if(task instanceof TaskDayDeal){
            rtv.setText(task.nameTask);
        } else if (task instanceof SettingAdapterTask) {
            rtv.setText("⚙️");
        } else {
            rtv.setText("📝" + task.nameTask);
        }


        drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(10);
        drawable.setColor(task.color);
        rtv.setBackground(drawable);
        return view;
    }
}
