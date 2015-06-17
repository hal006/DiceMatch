package com.example.hal.dicematch;

import android.app.Activity;
import android.content.Context;
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
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class HighScoreActivity extends Activity {


    private ArrayList<HashMap<String, String>> highScoreList;
    public static final String FIRST_COLUMN = "Name";
    public static final String SECOND_COLUMN = "Score";
    public static final int HIGH_SCORE_SIZE = 10;

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
            Log.e("INFO","chyba v onCreate HS" + e.toString());
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
            File file = new File(this.getFilesDir().getAbsolutePath() + "/high_score.dat");
            if (file.exists()) {
                Log.d("INFO", "exist ");
                FileInputStream fos = new FileInputStream(file.toString());
                Log.d("INFO", "exist ");
                ObjectInputStream ois = new ObjectInputStream(fos);
                Log.d("INFO", "exist ");
                HashMap<String, Serializable> loadedData;
                Log.d("INFO", "exist ");
                loadedData = (HashMap<String, Serializable>) ois.readObject();
                Log.d("INFO", "exist ");
                this.highScoreList = (ArrayList<HashMap<String, String>>) loadedData.get("1");
                Log.d("INFO", "exist ");
            } else {
                file = new File("high_score.dat");
                ArrayList<HashMap<String, String>> scores = new ArrayList<>();
                    for (int i = 0; i < HIGH_SCORE_SIZE; i++) {
                        HashMap<String, String> temp = new HashMap<>();
                        temp.put(FIRST_COLUMN, "Newbie");
                        temp.put(SECOND_COLUMN, "0");
                        scores.add(temp);
                    }
                this.highScoreList = scores;
                HashMap<String, Serializable> data = new HashMap<>();
                data.put("1", scores);
                FileOutputStream fos = openFileOutput(file.toString(), Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(data);
                oos.flush();
                oos.close();
                fos.close();
            }

        } catch (Exception e) {
            Log.e("INFO", "chyba v hs load " + e.toString());
        }
    }




}
