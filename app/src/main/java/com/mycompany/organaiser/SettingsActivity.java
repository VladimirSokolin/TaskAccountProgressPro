package com.mycompany.organaiser;

import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.ActionBar;
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

    DaoSettings daoSettings;
    MyDatabaseOpenHelper dataHelper;
    ListViewCustomizer listViewCustomizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialPreparations();
        launchListView();
    }

    private void initialPreparations(){
        dataHelper = new MyDatabaseOpenHelper(this);
        daoSettings = new DaoSettings(dataHelper);

        int color = daoSettings.getByTitle("color").value;

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
        drawableNavigateFocusButton.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        btSettings.setBackground(drawableNavigateFocusButton);
        btSettings.setTranslationY(-15);

        Window window = getWindow();
        window.setStatusBarColor(color);
        window.getDecorView().setBackgroundColor(color);
        window.setNavigationBarColor(color);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }

    }

    private void launchListView(){
        listView = findViewById(R.id.list_view_settings_activity);
        // this class responsible for displaying settings
        listViewCustomizer = new SimpleSetupListView(this, listView);
        listViewCustomizer.launchListView();
    }
}