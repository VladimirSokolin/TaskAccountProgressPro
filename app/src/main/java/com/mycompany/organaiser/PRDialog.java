package com.mycompany.organaiser;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class PRDialog extends Dialog {
     /*This method is used for setup layout of dialog, set date with them and so on*/
    interface PRDialogPreparer {
        void preparePRDialog(View layout);
    }

    private PRDialogPreparer preparer;
    View contentView;
    public PRDialog(@NonNull Context context, int themeResId) {
        super(context);
        contentView = LayoutInflater.from(context).inflate(themeResId, null);
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
    }

    public void showDialog() {
        if(preparer != null) {
            preparer.preparePRDialog(contentView);
        }
        show();
    }

    public void setPreparer(PRDialogPreparer preparer) {
        this.preparer = preparer;
    }


}
