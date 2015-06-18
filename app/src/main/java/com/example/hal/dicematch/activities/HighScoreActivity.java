package com.example.hal.dicematch.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.example.hal.dicematch.R;
import com.example.hal.dicematch.listViewSupport.HSListViewAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Activity loading and showing leaderboard from a file.
 */
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
            View.OnClickListener backHandler = new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            };
            Button b1 = (Button) findViewById(R.id.back_button);
            b1.setOnClickListener(backHandler);
            ListView listView = (ListView) findViewById(R.id.highscore_listview);
            this.loadScores();
            HSListViewAdapter adapter = new HSListViewAdapter(this, highScoreList);
            listView.setAdapter(adapter);
         } catch (Exception e) {
            Log.e("INFO","chyba v onCreate HS" + e.toString());
        }
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
                FileInputStream fos = new FileInputStream(file.toString());
                ObjectInputStream ois = new ObjectInputStream(fos);
                HashMap<String, Serializable> loadedData;
                loadedData = (HashMap<String, Serializable>) ois.readObject();
                this.highScoreList = (ArrayList<HashMap<String, String>>) loadedData.get("1");
            } else {
                // creates new high score file filled with 0 scores
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
