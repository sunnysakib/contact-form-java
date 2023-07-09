package com.example.sqlitedatabase_lab6_028;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;


public class CustomEventAdapter extends ArrayAdapter<Event> {

    final Context context;
    final ArrayList<Event> values;


    public CustomEventAdapter(@NonNull Context context, @NonNull ArrayList<Event> objects) {
        super(context, -1, objects);
        this.context = context;
        this.values = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_event, parent, false);

        TextView name = rowView.findViewById(R.id.name);
        TextView email = rowView.findViewById(R.id.email);
        TextView phoneHome = rowView.findViewById(R.id.phone);

        name.setText(values.get(position).name);
        email.setText(values.get(position).email);
        phoneHome.setText(values.get(position).phoneHome);
        return rowView;
    }
}
