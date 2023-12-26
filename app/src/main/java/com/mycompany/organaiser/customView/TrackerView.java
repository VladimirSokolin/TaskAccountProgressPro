package com.mycompany.organaiser.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import androidx.annotation.NonNull;
import com.mycompany.organaiser.R;
import com.mycompany.organaiser.TimeSpace;

import java.util.ArrayList;

public class TrackerView extends View {
  public static final String TAG = "TrackerView";

  private ArrayList<TimeSpace> timeSpacesPlane = new ArrayList<TimeSpace>();
  private ArrayList<TimeSpace> timeSpacesFact = new ArrayList<TimeSpace>();

  float width;
  float height;
  float padding = 20;
  float heightScale = 50;
  float distanceBetweenScales = 60;
  Scale scalePlane;
  Scale scaleFact;
  sPoint pointStartPlane;
  sPoint pointEndPlane;
  sPoint pointStartFact;
  sPoint pointEndFact;
  float distance;
  float scaleFactor;
  ScaleGestureDetector detector;
  private float previousDistanceBetweenFingers;
  int previousX = 0;
  public int sizeTimeSpacesFact = 0;
  public TrackerView(Context context) {
    super(context);
    init();
  }
  public TrackerView(Context context, AttributeSet attrs){
    super(context, attrs);
    init();
  }
  private void init(){
    detector = new ScaleGestureDetector(getContext(),  new ScaleListener());
    scalePlane = new Scale();
    scaleFact = new Scale();
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    width = w;
    height = h;
    pointStartPlane = new sPoint(padding, padding);
    pointEndPlane = new sPoint(width - padding, height/2 - distanceBetweenScales/2);
    pointStartFact = new sPoint(padding, height/2 + distanceBetweenScales/2);
    pointEndFact = new sPoint(width - padding, height - padding);

    scalePlane.setupSizesScale(pointStartPlane, pointEndPlane);
    scaleFact.setupSizesScale(pointStartFact, pointEndFact);
    super.onSizeChanged(w, h, oldw, oldh);
  }

  @Override protected void onDraw(@NonNull Canvas canvas) {
    super.onDraw(canvas);

    Paint paint = new Paint();
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(5);
    paint.setColor(Color.BLACK);

    for(TimeSpace timeSpace : timeSpacesPlane){
      scalePlane.drawTimeSpace(canvas, timeSpace);
    }
    for(TimeSpace timeSpace : timeSpacesFact){
      scaleFact.drawTimeSpace(canvas, timeSpace);;
    }

    scalePlane.drawScale(canvas, paint);
    scalePlane.drawScaleDivision(canvas);

    scaleFact.drawScale(canvas, paint);
    scaleFact.setHasScaleTime(true);
    scaleFact.drawScaleDivision(canvas);
    scaleFact.drawCursor(canvas);



    // draw line center
  }

  public void setNumberDivisions(int numberDivisions){
      scaleFact.setNumberDivisions(numberDivisions);
      scalePlane.setNumberDivisions(numberDivisions);
      invalidate();
  }

  public void setHourStart(int hourStart){
    scaleFact.setHourStart(hourStart);
    scalePlane.setHourStart(hourStart);
    invalidate();
  }

  public int getHourStart(){
    return  scalePlane.getHourStart();
  }
  public int getNumberDivisions(){
    return scaleFact.getNumberDivisions();
  }

  public void setTimeSpacesPlane(ArrayList<TimeSpace> timeSpaces){
      timeSpacesPlane = timeSpaces;
  }

  public void setTimeSpacesFact(ArrayList<TimeSpace> timeSpaces){
      timeSpacesFact = timeSpaces;
      sizeTimeSpacesFact = timeSpacesFact.size();
  }



  @Override
  public boolean onTouchEvent(MotionEvent event) {
    detector.onTouchEvent(event);
    int action = event.getAction();
    if(action == MotionEvent.ACTION_UP){
      previousDistanceBetweenFingers = 0;
      previousX = 0;
    }
    if(action == MotionEvent.ACTION_DOWN){
      Log.i(TAG, "onTouchEvent: ACTION_DOWN");
      previousX = (int) event.getX();
      Log.i(TAG, "onTouchEvent: previousX: " + previousX);
    }
    if(action == MotionEvent.ACTION_MOVE) {
      if(event.getPointerCount() == 1){
        int currentX = (int) event.getX();
        Log.i(TAG, "onTouchEvent: currentX: " + currentX);

        int delta = currentX - previousX;
        Log.i(TAG, "onTouchEvent: delta: " + delta);
        if (delta < -100) {
          setHourStart(scaleFact.getHourStart() + 1);
          previousX = currentX;
        }
        if (delta > 100) {
          setHourStart(scaleFact.getHourStart() - 1);
          previousX = currentX;
        }
      }
      }

    return true;
  }

  private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
      scaleFactor = detector.getScaleFactor();
      //Log.i(TAG, "onScale: detector.scaleFactor: " + scaleFactor);
      //Log.i(TAG, "onScale: previostDistanceBetweenFingers: " + previousDistanceBetweenFingers);
      float currentDistanceBetweenFingers = detector.getCurrentSpan();
      if(scaleFactor < 1) {
        float changedDistance = Math.abs(previousDistanceBetweenFingers - currentDistanceBetweenFingers);
       // Log.i(TAG, "onScale: changedDistance: " + changedDistance);
        if (changedDistance > 200) {
          setNumberDivisions(scalePlane.getNumberDivisions() + 1);
          previousDistanceBetweenFingers = currentDistanceBetweenFingers;
        }
      }
      if(scaleFactor > 1) {
        float changedDistance = Math.abs(previousDistanceBetweenFingers - detector.getCurrentSpan());
        //Log.i(TAG, "onScale: changedDistance: " + changedDistance);
        if(changedDistance > 200){
          setNumberDivisions(scalePlane.getNumberDivisions() - 1);
          previousDistanceBetweenFingers = currentDistanceBetweenFingers;
        }
      }
      return true;
    }
  }

}
