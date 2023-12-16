package com.mycompany.organaiser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

public class SettingsActivity extends AppCompatActivity {

    ListView listView;
    Button btMenu;
    Button btTracker;
    Button btNotePad;
    Button btSettings;
    Button btProgress;
    ListViewCustomizer listViewCustomizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialPreparations();
        launchListView();
    }

    private void initialPreparations(){
        btMenu = findViewById(R.id.bt_navigation_menu);
        btMenu.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            finish();
        });
        btTracker = findViewById(R.id.bt_navigation_tracker);
        btTracker.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, CommitActivity.class));
            finish();
        });
        btNotePad = findViewById(R.id.bt_navigation_notePad);
        btSettings = findViewById(R.id.bt_navigation_settings);
        btProgress = findViewById(R.id.bt_navigation_achievements);

        Drawable drawableNavigateFocusButton = getDrawable(R.drawable.shape_view_navigate_focus);
        drawableNavigateFocusButton.setColorFilter(getResources().getColor(R.color.item_task_main), android.graphics.PorterDuff.Mode.SRC_IN);
        btSettings.setBackground(drawableNavigateFocusButton);
        btSettings.setTranslationY(-15);

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.item_task_main));
        window.getDecorView().setBackgroundColor(getResources().getColor(R.color.item_task_main));
        window.setNavigationBarColor(getResources().getColor(R.color.item_task_main));
    }

    private void launchListView(){
        listView = findViewById(R.id.list_view_settings_activity);
        listViewCustomizer = new SimpleSetupListView(this, listView);
        listViewCustomizer.launchListView();
    }
}