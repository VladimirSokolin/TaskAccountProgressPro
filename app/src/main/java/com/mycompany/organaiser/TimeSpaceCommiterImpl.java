package com.mycompany.organaiser;

import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import java.util.ArrayList;

public class TimeSpaceCommiterImpl implements TimeSpaceCommiter{
    @Override
    public void commitTimeSpace(TimeSpace timeSpace, TextView tv) {
        tv.append(timeSpace.timeStart + "\n");
        tv.append(Html.fromHtml("<font color='" + timeSpace.color + "'><big>" + timeSpace.nameDeal + "</big></font>"));
        if(timeSpace.description != null){
           String s = "<html><br>" + timeSpace.description + "</html>";
            tv.append(Html.fromHtml(s));

        }
        tv.append("\n" + timeSpace.timeStop + "\n————————————————\n");
    }


    @Override
    public void rangeAndCommitAllTimeSpaces(ArrayList<TimeSpace> timeSpaces, TextView textView) {
        // sort the list of timeSpaces so that all TimeSpace.timeMiStart go from smallest to largest time
        timeSpaces.sort((o1, o2) -> {
            if(o1.timeMlStart > o2.timeMlStart){
                return 1;
            } else if(o1.timeMlStart < o2.timeMlStart){
                return -1;
            }
            return 0;
        });
        timeSpaces.forEach(timeSpace -> {
            commitTimeSpace(timeSpace, textView);
        });
    }
}
