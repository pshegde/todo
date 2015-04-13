package com.secondapp.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Prajakta on 4/12/2015.
 */
public class TodoAdapter extends ArrayAdapter<TodoItem> {
    public TodoAdapter(Context context, List<TodoItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TodoItem item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }
        // Lookup view for data population
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        // Populate the data into the template view using the data object
        tvBody.setText(item.getBody());
        tvPriority.setText(String.valueOf(item.getPriority()));
        // Return the completed view to render on screen
        return convertView;
    }
}
