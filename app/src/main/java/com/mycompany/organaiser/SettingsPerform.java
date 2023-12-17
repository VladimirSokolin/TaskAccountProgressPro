package com.mycompany.organaiser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import yuku.ambilwarna.AmbilWarnaDialog;

import static androidx.core.content.ContextCompat.startActivity;

public class SettingsPerform implements View.OnClickListener {
    Context context;
    DaoSettings daoSettings;
    int color;
        //TODO Get color from database
    public SettingsPerform(Context context) {
        this.context = context;
        daoSettings = new DaoSettings(context);
    }
    @Override
    public void onClick(View v) {

        TextView tv = v.findViewById(R.id.tv_title_settings);
        if(tv.getText().equals("Set color of app")) {
            Setting setting = daoSettings.getByTitle("color");
            DialogSettingColorApp dialogSettingColorApp = new DialogSettingColorApp(context, R.style.FullScreenDialogTheme,setting, ()-> {
              if(context instanceof SettingsActivity){
                  Intent intent = new Intent(context, SettingsActivity.class);
                  ((SettingsActivity)context).finish();
                  context.startActivity(intent);
              }
            });
            dialogSettingColorApp.show();

            /**/
        }
    }
}
