package com.mycompany.organaiser;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogSettingEditText extends Dialog {

  Context context;
  /**This class may to work with each setting fot input. else do you want
   * change type edit text of dialog - to use override method {@code setEditTextType() } */
  public DialogSettingEditText(Context context, int style, Setting setting, int color) {
    super(context, style);

    this.context = context;

    View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_settings_edit_text, null);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(contentView);
    if(getWindow() != null) {
      getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
    layoutParams.copyFrom(getWindow().getAttributes());
    int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
    layoutParams.width = context.getResources().getDisplayMetrics().widthPixels - 2 * margin;
    getWindow().setAttributes(layoutParams);

    LinearLayout linear = (LinearLayout) contentView.findViewById(R.id.layout_dialog_settings_edit_text);
    if(linear != null) {
      GradientDrawable gradientDrawable = new GradientDrawable();
      gradientDrawable.setColor(color);
      gradientDrawable.setCornerRadius(30);
      linear.setBackground(gradientDrawable);
    }

    TextView textView = contentView.findViewById(R.id.dialog_setting_text_view);
    EditText editText = contentView.findViewById(R.id.dialog_setting_edit_text);
    setEditTextType(editText);
    Button btOk = contentView.findViewById(R.id.bt_ok_dialog_setting);
    Button btCancel = contentView.findViewById(R.id.bt_cancel_dialog_setting);

    textView.setText(setting.title);
    editText.setHint(String.valueOf(setting.value));

    btOk.setOnClickListener(view -> {
      performOnClickOk(setting, editText);
      cancel();
    });

    btCancel.setOnClickListener(view -> cancel());
  }

  public void setEditTextType(EditText et){
    //et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
  }

  public void performOnClickOk(Setting setting, EditText editText){
    //Override it
  }
}
