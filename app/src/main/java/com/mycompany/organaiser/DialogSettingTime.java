package com.mycompany.organaiser;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

public class DialogSettingTime extends DialogSettingEditText{
  public DialogSettingTime(Context context, int style, Setting setting, int color){
    super(context, style, setting, color);
  }

  @Override
  public void setEditTextType(EditText et) {
    et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
  }

  @Override
  public void performOnClickOk(Setting setting, EditText editText) {
    int value = Integer.parseInt(editText.getText().toString());
    if(value >= 0 && value <= 23){
      setting.value = value;
      new DaoSettings(context).update(setting);
    } else {
      editText.setError("Incorrect time");
    }
    cancel();
  }

}
