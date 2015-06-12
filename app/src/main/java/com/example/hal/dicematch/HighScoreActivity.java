package com.example.hal.dicematch;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.example.hal.dicematch.util.ListViewScoreAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class HighScoreActivity extends Activity {


    private ArrayList<HashMap<String, String>> highScoreList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.activity_high_score);
            //Remove notification bar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Bundle extras = getIntent().getExtras();
//            if (extras != null) {
//
//            }
            View.OnClickListener backHandler = new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            };
            Button b1 = (Button) findViewById(R.id.back_button);
            b1.setOnClickListener(backHandler);
            ListView listView = (ListView) findViewById(R.id.highscore_listview);
            this.loadScores();
            ListViewScoreAdapter adapter = new ListViewScoreAdapter(this, highScoreList);
            listView.setAdapter(adapter);
         } catch (Exception e) {
            Log.d("INFO",e.toString());
        }
    }

    public ArrayList<HashMap<String, String>> getList () {
        return highScoreList;
    }

    public void setList (ArrayList<HashMap<String, String>> list) {
        this.highScoreList=list;
    }

    @Override
    protected void onStop () {
        super.onStop();
    }
    @Override
    protected void onDestroy () {
        super.onDestroy();
    }

    @SuppressWarnings("unchecked")
    void loadScores() {
        try {
            File file = new File(this.getFilesDir().getAbsolutePath() + "/high_scores.dat");
            FileInputStream fos = new FileInputStream(file.toString());
            ObjectInputStream ois = new ObjectInputStream(fos);
            HashMap<String, Serializable> loadedData;
            loadedData = (HashMap<String, Serializable>) ois.readObject();
            this.highScoreList = (ArrayList<HashMap<String, String>>) loadedData.get("1");

        } catch (Exception e) {
            Log.d("INFO", e.toString());
        }
    }




}
