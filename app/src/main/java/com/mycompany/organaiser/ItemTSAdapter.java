package com.mycompany.organaiser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ItemTSAdapter extends ArrayAdapter<TimeSpace> {
    Context mContext;
    OnItemTimeSpaceDeleteListener deleter;
    public ItemTSAdapter(Context context, ArrayList<TimeSpace> list, OnItemTimeSpaceDeleteListener deleter) {
        super(context, R.layout.entity, list);
        mContext = context;
        this.deleter = deleter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TimeSpace ts = getItem(position);

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_time_space_dayrouting, parent, false);
        final TextView tvTimeStart = view.findViewById(R.id.tv_time_start_time_space_dayrouting);
        final TextView tvTimeStop = view.findViewById(R.id.tv_time_stop_time_space_dayrouting);
        final TextView tvName = view.findViewById(R.id.tv_name_time_space_dayrouting);
        final TextView tvDescription = view.findViewById(R.id.tv_description_time_space_dayrouting);
        final ImageView ivDelete = view.findViewById(R.id.iv_delete_time_space_dayrouting);

        tvTimeStart.setText(ts.timeStart);
        tvTimeStop.setText(ts.timeStop);
        tvName.setText(ts.nameDeal);
        tvDescription.setText(ts.description);

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleter.deleteTimeSpace(ts);
                notifyDataSetChanged();
            }
        });

        return view;

    }
}
