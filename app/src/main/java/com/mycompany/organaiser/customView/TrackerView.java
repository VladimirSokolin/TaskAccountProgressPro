package com.mycompany.organaiser.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import com.mycompany.organaiser.R;

public class TrackerView extends View {

  float width;
  float height;
  float padding = 20;
  float heightScale = 50;
  float distanceBetweenScales = 60;
  Scale scalePlane;
  Scale scaleFact;
  public TrackerView(Context context) {
    super(context);
  }
  public TrackerView(Context context, AttributeSet attrs){
    super(context, attrs);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    width = w;
    height = h;
    scalePlane = new Scale(new sPoint(padding, padding), new sPoint(width - padding, height/2 - distanceBetweenScales/2 ));
    scaleFact = new Scale(new sPoint(padding, height/2 + distanceBetweenScales/2), new sPoint(width - padding, height - padding));
    super.onSizeChanged(w, h, oldw, oldh);
  }

  @Override protected void onDraw(@NonNull Canvas canvas) {
    super.onDraw(canvas);

    //draw drawable on canvas
    Drawable drawable = getResources().getDrawable(R.drawable.shape_view_in_dialog);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);

    Paint paint = new Paint();
    paint.setColor(Color.BLACK);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(5);

    scalePlane.drawScale(canvas, paint);
    scaleFact.drawScale(canvas, paint);

    // draw line center
    paint.setColor(Color.RED);
    canvas.drawLine(padding, height/2, width - padding, height/2, paint);
  }
}
