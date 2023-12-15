package com.mycompany.organaiser;

import android.view.View;
import android.view.ViewGroup;

public interface ViewCreater<T> {
    public View createView(int position, View convertView, ViewGroup parent, T object);
}
