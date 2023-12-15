package com.mycompany.organaiser;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UniversalAdapter<T> extends ArrayAdapter<T> {

    ViewCreater viewCreater;
    public UniversalAdapter(android.content.Context context, int textViewResourceId, ArrayList<T> objects, ViewCreater viewCreater) {
        super(context, textViewResourceId, objects);
        this.viewCreater = viewCreater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        T item = getItem(position);
        return viewCreater.createView(position, convertView, parent, item);
    }
}
