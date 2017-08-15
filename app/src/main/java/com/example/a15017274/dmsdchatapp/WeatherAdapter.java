package com.example.a15017274.dmsdchatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 15017274 on 1/8/2017.
 */

public class WeatherAdapter extends ArrayAdapter<Weather> {
    private ArrayList<Weather> w;
    private Context context;
    private TextView tvUser, tvText, tvTime;

    public WeatherAdapter(Context context, int resource, ArrayList<Weather> objects){
        super(context, resource, objects);
        w = objects;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row, parent, false);

        tvText = (TextView) rowView.findViewById(R.id.tvText);
        tvUser = (TextView) rowView.findViewById(R.id.tvUser);
        tvTime = (TextView) rowView.findViewById(R.id.tvTime);

        Weather currW = w.get(position);

        tvText.setText(currW.getText());
        tvTime.setText(currW.getTime());
        tvUser.setText(currW.getUserName());
        return rowView;
    }
}
