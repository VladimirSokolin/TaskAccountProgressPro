package com.mycompany.organaiser;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

public class PickColorPRDialog implements PRDialog.PRDialogPreparer {
    Context context;
    Dialog dialog;
    FragmentManager fragmentManager;
    public PickColorPRDialog(Context context) {
        this.context = context;
    }
    @Override
    public void preparePRDialog(View layout) {
            LinearLayout linear = (LinearLayout) layout.findViewById(R.id.layout_dialog_fast_note);
            if(linear != null) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(context.getResources().getColor(R.color.item_task_main));
                gradientDrawable.setCornerRadius(30);
                linear.setBackground(gradientDrawable);
            }

            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(20);


            Button btOk = layout.findViewById(R.id.bt_ok_dialog_new_task_color);
            Button btCancel = layout.findViewById(R.id.bt_cancel_dialog_new_task_color);
            TextView tvCurrentColor = layout.findViewById(R.id.tv_dialog_new_task_color);
            tvCurrentColor.setBackgroundColor(Color.rgb(145, 49, 81));
            GridView gridView = layout.findViewById(R.id.grid_view_dialog_new_task_color);
            //TODO Setup GRIDVIEW
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });

            if(dialog == null){
                throw new RuntimeException("Dialog is null, call setDialog");
            }
            btOk.setOnClickListener((v) -> {
                dialog.dismiss();
            });
            btCancel.setOnClickListener((v) -> {
                dialog.cancel();
            });
    }

    public void setDialog(Dialog dialog){
        this.dialog = dialog;
    }
}
