package com.example.hal.dicematch.listViewSupport;

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
import android.widget.Button;
import android.widget.TextView;

import com.example.hal.dicematch.R;


/**
 * Custom adapter for two column ListView (String, String),
 * supporting now only ScoreFragment
 */
public class ScoreListViewAdapter extends ArrayAdapter<HashMap<String, String>> {

    public static final String FIRST_COLUMN="Choice";
    public static final String SECOND_COLUMN="Score";
    Context context;
    List<Boolean> submitedScore = new ArrayList<Boolean>();

    public ArrayList<HashMap<String, String>> list;
    TextView txtFirst;
    TextView txtSecond;

    public ScoreListViewAdapter(List<Boolean> submitedScores, Context context, ArrayList<HashMap<String, String>> list) {
        super(context, R.layout.column_row, list);
        this.submitedScore = submitedScores;
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
        convertView = inflater.inflate(R.layout.column_row, parent, false);
        try {

        if (!submitedScore.get(position)) {
            convertView.setBackground(parent.getResources().getDrawable(R.drawable.custom_lv_sorbus));
        }

        } catch (Exception e) {
            Log.d("INFO",e.toString());
        }
        txtFirst = (TextView) convertView.findViewById(R.id.scoreName);
        txtSecond = (TextView) convertView.findViewById(R.id.scoreValue);


        if (convertView == null) {


            txtFirst = (TextView) convertView.findViewById(R.id.scoreName);
            txtSecond = (TextView) convertView.findViewById(R.id.scoreValue);
        }

            HashMap<String, String> map = list.get(position);
            txtFirst.setText(map.get(FIRST_COLUMN));
            txtSecond.setText(map.get(SECOND_COLUMN));

        return convertView;
    }
}