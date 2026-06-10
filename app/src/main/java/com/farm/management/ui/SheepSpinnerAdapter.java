package com.farm.management.ui;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.farm.management.models.Sheep;

import java.util.List;

public class SheepSpinnerAdapter extends ArrayAdapter<Sheep> {

    public SheepSpinnerAdapter(Context context, List<Sheep> sheepList) {
        super(context, android.R.layout.simple_spinner_item, sheepList);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public String getItem(int position) {
        Sheep sheep = super.getItem(position);
        if (sheep != null) {
            return sheep.tagNumber + " (" + sheep.gender + ")";
        }
        return "";
    }
}
