package com.mycompany.organaiser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

public class ColorView extends View {
    String symbol;

    public ColorView(Context context) {
        super(context);
    }
    public ColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if(symbol != null){
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(54);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(symbol, getWidth() / 2, getHeight() / 2 + 20, paint);
        }

    }
    public void setSymbol(String symbol){
        this.symbol = symbol;
    }
}
