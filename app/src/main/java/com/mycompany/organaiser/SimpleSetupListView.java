package com.mycompany.organaiser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SimpleSetupListView implements ListViewCustomizer {

    View.OnClickListener listener;
    private UniversalAdapter<Setting> universalAdapter;
    private ViewCreater viewCreater;
    private ArrayList<Setting> settings;
    private ListView listView;
    public SimpleSetupListView(Context context, ListView listView) {
        this.listView = listView;
        listener = (v) -> {
            TextView tv = v.findViewById(R.id.tv_title_settings);
            if(tv.getText().equals("Set color of app")) {
                DialogChooseColor dialog = new DialogChooseColor();
            }
            /* It’s very strange that I need to put the dialogue inside:

            PRDialog prDialog = new PRDialog(context, R.layout.dialog_new_task_color);
            PickColorPRDialog colorDialog = new PickColorPRDialog(context);
            colorDialog.setDialog(prDialog);
            prDialog.setPreparer(colorDialog);
            prDialog.showDialog();*/
        };
        viewCreater = (position, convertView, parent, object) -> {
            Setting setting = (Setting) object;
            View view = LayoutInflater.from(context).inflate(R.layout.item_settings, parent, false);
            TextView tvTitle = view.findViewById(R.id.tv_title_settings);
            TextView tvDescription = view.findViewById(R.id.tv_description_settings);
            tvTitle.setText(setting.title);
            tvDescription.setText(setting.description);

            view.setOnClickListener((v) -> {
                listener.onClick(v);
            });

            return view;
        };

        settings = new ArrayList<>();
        settings.add(new Setting("Set color of app", "This setting will change the color of the app"));
        settings.add(new Setting("Set size text of app", "This setting will change the size of the text"));
        universalAdapter = new UniversalAdapter<>(context, R.layout.item_settings, settings, viewCreater);
    }

    public void setSettings(ArrayList<Setting> settings){
        this.settings = settings;
    }

    public void setUniversalAdapter(UniversalAdapter<Setting> universalAdapter){
        this.universalAdapter = universalAdapter;
    }

    @Override
    public void launchListView() {
        listView.setAdapter(universalAdapter);
    }
}
