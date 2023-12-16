package com.mycompany.organaiser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;

public class ProgressTaskView extends View {

    int width;
    int height;
    int color = Color.GRAY;
    double currentCount;
    double fullCount;
    public ProgressTaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();

        Paint paint = new Paint();
        paint.setColor(calculateColorFromCount());

        if(currentCount != 0 && fullCount != 0){
            canvas.drawRoundRect(0, 0, (int)(width * currentCount / fullCount), height, 6, 6, paint);
        }


    }
    public void setColor(int color){
        this.color = color;
    }
    public void setCount(double currentCount, double fullCount){
        this.currentCount = currentCount;
        this.fullCount = fullCount;
    }

    private int calculateColorFromCount(){
        if(currentCount != 0 && fullCount != 0){
            int proportion = (int)(currentCount / fullCount * 120);

            Log.e(MainActivity.TAG, "calculateColorFromCount: " + proportion + ", currentCount " + currentCount + ", fullCount " + fullCount);
            float[] hsv = new float[]{proportion, 10, 89};
            return Color.HSVToColor(hsv);

            /*if(proportion <= 25){
                return Color.rgb();
            } else if(proportion <= 50){
                return Color.rgb(255, 179,0);
            } else if(proportion <= 75){
                return Color.YELLOW;
            } else {
                return Color.GREEN;
            }*/
        }
        return color;
    }
}
