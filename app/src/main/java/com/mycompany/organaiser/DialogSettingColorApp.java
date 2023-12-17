package com.mycompany.organaiser;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import yuku.ambilwarna.AmbilWarnaDialog;

public class DialogSettingColorApp extends Dialog {

  private final View screenDemonstrationView;

  interface ListenerOk {
    void onOk();
  }

  ListenerOk okListener;
  int colorInt;
  GradientDrawable drawable;
  public DialogSettingColorApp(Context context, int style, Setting setting, ListenerOk okListener) {
    super(context, style);
    this.okListener = (ListenerOk) okListener;
    colorInt = setting.value;
    View view = getLayoutInflater().inflate(R.layout.dialog_settings_color_app, null);
    screenDemonstrationView = view.findViewById(R.id.constraint_screen);
    drawable = new GradientDrawable();
    drawable.setCornerRadius(40);
    drawable.setColor(colorInt);
    screenDemonstrationView.setBackground(drawable);
    setContentView(view);

    TextView tvPickColor = view.findViewById(R.id.tv_settings_color_app);
    Button btOk = view.findViewById(R.id.bt_settings_color_app_ok);
    Button btCancel = view.findViewById(R.id.bt_settings_color_app_cancel);

    tvPickColor.setOnClickListener(v -> {
      AmbilWarnaDialog warnaDialog = new AmbilWarnaDialog(context, (0xffff0000), new AmbilWarnaDialog.OnAmbilWarnaListener() {
        @Override
        public void onCancel(AmbilWarnaDialog dialog) {
          dialog.getDialog().cancel();
        }
        @Override
        public void onOk(AmbilWarnaDialog dialog, int color) {
          colorInt = color;
          drawable.setColor(color);
        }
      });
      warnaDialog.show();
    });

    btOk.setOnClickListener(v -> {
      setting.value = colorInt;
      new DaoSettings(context).update(setting);
      okListener.onOk();
      dismiss();
    });

    btCancel.setOnClickListener(v -> {
      cancel();
    });
  }

  public void setColorScreenDemonstration(int color){
    drawable.setColor(color);
  }
}
