package com.example.hal.dicematch.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hal.dicematch.R;

public class ListViewScoreAdapter extends ArrayAdapter<HashMap<String, String>> {

    public static final String FIRST_COLUMN="Name";
    public static final String SECOND_COLUMN="Score";

    Context context;

    public ArrayList<HashMap<String, String>> list;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;

    public ListViewScoreAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        super(context, R.layout.highscore_row, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.highscore_row, parent, false);
            txtFirst = (TextView) convertView.findViewById(R.id.hs_order);
            txtSecond = (TextView) convertView.findViewById(R.id.hs_name);
            txtThird = (TextView) convertView.findViewById(R.id.hs_value);
        }

        txtFirst = (TextView) convertView.findViewById(R.id.hs_order);
        txtSecond = (TextView) convertView.findViewById(R.id.hs_name);
        txtThird = (TextView) convertView.findViewById(R.id.hs_value);

        HashMap<String, String> map = list.get(position);
        txtFirst.setText(""+ (position + 1)+".");
        txtSecond.setText(map.get(FIRST_COLUMN));
        txtThird.setText(map.get(SECOND_COLUMN));

        return convertView;
    }
}