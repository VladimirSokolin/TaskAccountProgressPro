package com.mycompany.organaiser.customView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

public class BorderEditText extends androidx.appcompat.widget.AppCompatEditText {
    public BorderEditText(Context context) {
        super(context);
        init();
    }

    public BorderEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(5, Color.BLACK);
        drawable.setCornerRadius(20);

        setHintTextColor(Color.BLACK);
        // сделать так, чтобы stroke была на расстоянии 5 от всех краев
        setPadding(30, 30, 30, 40);

        //setBackground(drawable);
    }
}
