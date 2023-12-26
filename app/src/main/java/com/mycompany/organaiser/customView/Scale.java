package com.mycompany.organaiser.customView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.mycompany.organaiser.TimeSpace;

import java.util.Calendar;

public class Scale {

  final int MAX_TEXT_SIZE = 50;
  final int MIN_TEXT_SIZE = 20;
  sPoint pointStart;
  sPoint pointEnd;
  float width;
  float height;
  float step;
  private int numberDivisions = 15;
  private int hourStart = -2;
  private int textSize;
  private float relativelyPositionCursor;
  private boolean hasScaleTime = false;

  Scale(float xStart, float yStart, float xEnd, float yEnd) {
    pointStart = new sPoint(xStart, yStart);
    pointEnd = new sPoint(xEnd, yEnd);
    width = xEnd - xStart;
    height = yEnd - yStart;
    step = width/numberDivisions;
    calculateSizeText();
  }

  public Scale(){

  }

  Scale(sPoint pointStart, sPoint pointEnd) {
    this.pointStart = pointStart;
    this.pointEnd = pointEnd;
    width = pointEnd.x - pointStart.x;
    height = pointEnd.y - pointStart.y;
    step = width/numberDivisions;
    calculateSizeText();
  }

  public void setupSizesScale(sPoint pointStart, sPoint pointEnd){
    this.pointStart = pointStart;
    this.pointEnd = pointEnd;
    width = pointEnd.x - pointStart.x;
    height = pointEnd.y - pointStart.y;
    step = width/numberDivisions;
    calculateSizeText();
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

  void drawScaleDivision(Canvas canvas){
    Paint p = new Paint();
    p.setColor(Color.BLACK);
    p.setStyle(Paint.Style.STROKE);
    p.setStrokeWidth(2);
    int hour = hourStart;
    float adjustedY2 = pointEnd.y - 5;
    float adjustedY1 = pointStart.y + 5;
    if(hasScaleTime){
      for(float f = pointStart.x; f < pointEnd.x; f += step) {
        if(hour > 23) hour = 0;
        canvas.drawLine(f, adjustedY1, f, adjustedY2, p);
        drawScaleTime(canvas, f, hour);
        hour++;
      }
    }else{
      for(float f = pointStart.x; f < pointEnd.x; f += step) {
        canvas.drawLine(f, adjustedY1, f, adjustedY2, p);
      }
    }
  }
  private void drawScaleTime(Canvas canvas, float f, int hour){
      Paint p = new Paint();
      p.setTextSize(textSize);
      p.setColor(Color.BLACK);
      p.setStyle(Paint.Style.FILL);
      p.setTextAlign(Paint.Align.CENTER);

      int delta = 45;
      int x = (textSize - MIN_TEXT_SIZE)/2;
      canvas.drawText(String.valueOf(hour), f, pointStart.y - (delta + x - textSize), p);
  }
  void drawCursor(Canvas canvas){
    Paint p = new Paint();
    p.setColor(Color.RED);
    p.setStyle(Paint.Style.STROKE);
    p.setStrokeWidth(5);
    calculatePositionOfTimeCursor();
    if(relativelyPositionCursor < pointStart.x || relativelyPositionCursor > pointEnd.x) return;
    canvas.drawLine(relativelyPositionCursor + pointStart.x, pointStart.y, relativelyPositionCursor + pointStart.x, pointEnd.y, p);
  }

  void drawTimeSpace(Canvas canvas, TimeSpace ts){
    Paint paint = new Paint();
    paint.setStyle(Paint.Style.FILL);
    paint.setColor(ts.getColor());

    float left = calculateTimeSpace(ts.conversationStringToMl(ts.timeStart), true);
    float right = calculateTimeSpace(ts.conversationStringToMl(ts.timeStop), false);

    if(left > right){
      canvas.drawRect(pointStart.x, pointStart.y +5, right + pointStart.x, pointEnd.y - 5, paint);
      return;
    }
    if(left != right){
      canvas.drawRect(left + pointStart.x, pointStart.y +5, right + pointStart.x, pointEnd.y - 5, paint);
    }

  }

  float calculateTimeSpace(long time, boolean isLeft){
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    long timeStartDay = calendar.getTimeInMillis();
    long currentTime = time;
    float deltaTime = currentTime - timeStartDay;
    float timeInScale = 3_600_000L * numberDivisions;
    float positionFromZero = (deltaTime / timeInScale) * width;
    float positionFromStartHour = positionFromZero - (step * hourStart);
    float pointsInScale = 24 * step;

    if(positionFromStartHour < 0 && positionFromStartHour + pointsInScale < pointEnd.x){
      positionFromStartHour += pointsInScale;
    }
    if(positionFromStartHour + pointStart.x < pointStart.x){
      positionFromStartHour = 0;
    }
    if(positionFromStartHour + pointStart.x > pointEnd.x) {
      positionFromStartHour = pointEnd.x - pointStart.x;
    }

    return positionFromStartHour;
  }

  void calculateSizeText(){
    textSize = (int)step/2;
    if(textSize > MAX_TEXT_SIZE){
      textSize = MAX_TEXT_SIZE;
    }
    if(textSize < MIN_TEXT_SIZE){
      textSize = MIN_TEXT_SIZE;
    }
  }

  void calculatePositionOfTimeCursor(){
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    long timeStartDay = calendar.getTimeInMillis();
    long currentTime = System.currentTimeMillis();
    float deltaTime = currentTime - timeStartDay;
    float timeInScale = 3_600_000L * numberDivisions;
    float positionFromZero = (deltaTime / timeInScale) * width;
    float positionFromStartHour = positionFromZero - (step * hourStart);
    if(positionFromStartHour < 0) positionFromStartHour += step * 24;
    relativelyPositionCursor = positionFromStartHour;
  }
  public void setHourStart(int hourStart){
    if(hourStart < 0) hourStart = 23;
    if(hourStart > 23) hourStart = 0;
    this.hourStart = hourStart;
  }
  public int getHourStart(){
    return hourStart;
  }

  public void setNumberDivisions(int numberDivisions){
    this.numberDivisions = numberDivisions % 24;
    step = width/numberDivisions;
    calculateSizeText();
  }

  public void setHasScaleTime(boolean hasScaleTime) {
    this.hasScaleTime = hasScaleTime;

  }

  public int getNumberDivisions(){
    return numberDivisions;
  }
}
