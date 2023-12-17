package com.mycompany.organaiser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
public class SimpleSetupListView implements ListViewCustomizer {

    /**
     * This class responsible for managing ListView for {@link SettingsActivity}*/

    /**
     * listener catches a click on the setting and transfers further
     * execution of the settings to a separate class - {@link SettingsPerform} */
    View.OnClickListener listener;
    private UniversalAdapter<Setting> universalAdapter;
    private ViewCreater viewCreater;
    private ArrayList<Setting> settings;
    private ListView listView;

    public SimpleSetupListView(Context context, ListView listView) {
        this.listView = listView;
        listener = new SettingsPerform(context); // this class contents logic of settings
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
        settings.add(new Setting("Set color of app", "This setting will change the color of the app", 1));
        settings.add(new Setting("Set size text of app", "This setting will change the size of the text", 1));
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
