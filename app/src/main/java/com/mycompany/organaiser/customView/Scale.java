package com.mycompany.organaiser.customView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Scale {
  sPoint pointStart;
  sPoint pointEnd;
  float width;
  float height;
  Scale(float xStart, float yStart, float xEnd, float yEnd) {
    pointStart = new sPoint(xStart, yStart);
    pointEnd = new sPoint(xEnd, yEnd);
  }
  Scale(sPoint pointStart, sPoint pointEnd) {
    this.pointStart = pointStart;
    this.pointEnd = pointEnd;
    width = pointEnd.x - pointStart.x;
    height = pointEnd.y - pointStart.y;
  }

  void drawScale(Canvas canvas, Paint paint){
    canvas.drawRoundRect(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y, 10, 10, paint);
  }
  void drawScale(Canvas canvas){
    Paint p = new Paint();
    p.setColor(Color.BLACK);
    p.setStyle(Paint.Style.STROKE);
    canvas.drawRoundRect(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y, 10, 10, p);
  }


}
