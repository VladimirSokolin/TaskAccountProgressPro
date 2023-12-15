package com.mycompany.organaiser;

import android.widget.TextView;

import java.util.ArrayList;

public interface TimeSpaceCommiter {
    public void commitTimeSpace(TimeSpace timeSpace, TextView textView);
    public void rangeAndCommitAllTimeSpaces(ArrayList<TimeSpace> timeSpaces, TextView textView);
}
