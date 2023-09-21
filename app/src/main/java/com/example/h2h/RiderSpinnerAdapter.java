package com.example.h2h;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class RiderSpinnerAdapter extends ArrayAdapter<Rider> {

    private Context context;
    private List<Rider> riderList;

    public RiderSpinnerAdapter(Context context, int resource, List<Rider> riderList) {
        super(context, resource, riderList);
        this.context = context;
        this.riderList = riderList;
    }

    @Override
    public int getCount() {
        return riderList.size();
    }

    @Override
    public Rider getItem(int position) {
        return riderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView riderNameTextView = convertView.findViewById(android.R.id.text1);

        Rider rider = riderList.get(position);
        riderNameTextView.setText(rider.getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView riderNameTextView = convertView.findViewById(android.R.id.text1);

        Rider rider = riderList.get(position);
        riderNameTextView.setText(rider.getName());

        return convertView;
    }
}
