package com.example.appbanthietbidientu.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    public CustomSpinnerAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    @Override
    public boolean isEnabled(int position) {
        // Trả về false để ngăn chặn việc chọn mục trong Spinner
        return false;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // Disabling dropdown view to prevent selection
        View view = super.getDropDownView(position, convertView, parent);
        view.setEnabled(false);
        return view;
    }
}
