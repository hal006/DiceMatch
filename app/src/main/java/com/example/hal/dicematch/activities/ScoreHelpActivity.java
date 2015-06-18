package com.example.hal.dicematch.activities;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hal.dicematch.R;
import com.example.hal.dicematch.listViewSupport.HelpListViewAdapter;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Activity showing game help.
 */
public class ScoreHelpActivity extends Activity {

    private static final String TUTORIAL_TEXT = "This game is a remake of the original Yahtzee dice game. Main goal of the game is to get as much points as possible in 13 turns. " +
            "Turns consist of 3 consequent rolls with 5 or less dice. Each roll, player chooses which dice to reroll by selecting it. " +
            "After 3 rolls or after finishing the roll with a button, player has to pick a category. " +
            "Each category (row in score list) can be selected only once and has its own unique way to calculate score (as described below).";
    private static final String TUTORIAL_TEXT_HEADING = "How to play this game:";
    private ArrayList<HashMap<String, String>> highScoreHelpList;
    public static final String FIRST_COLUMN = "Name";
    public static final String SECOND_COLUMN = "Score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.activity_score_help);
            //Remove notification bar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Bundle extras = getIntent().getExtras();
            View.OnClickListener backHandler = new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            };
            Button b1 = (Button) findViewById(R.id.backHelpButton);
            b1.setOnClickListener(backHandler);
            TextView t1 = (TextView) findViewById(R.id.helpText);
            t1.setText(TUTORIAL_TEXT);
            TextView t2 = (TextView) findViewById(R.id.helpTextHeading);
            t2.setText(TUTORIAL_TEXT_HEADING);
            ListView listView = (ListView) findViewById(R.id.listHelpView);
            this.readXMLdata();
            Log.d("INFO", highScoreHelpList.size()+"");
            HelpListViewAdapter adapter = new HelpListViewAdapter(this, highScoreHelpList);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            Log.d("INFO", e.toString());
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

    /**
     * Loads score tutorial from the XML file.
     */
    private void readXMLdata() {
        XmlResourceParser xrp = this.getResources().getXml(R.xml.categories);
        highScoreHelpList = new ArrayList<>();
        try {
            xrp.next();
            int eventType = xrp.getEventType();
            HashMap<String, String> tempMap = new HashMap<>();
            String tmp;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG
                        && xrp.getName().equalsIgnoreCase("name")) {
                    xrp.next();
                    tmp = xrp.getText();
                    tempMap = new HashMap<>();
                    tempMap.put(FIRST_COLUMN, tmp);
                } else {
                    if (eventType == XmlPullParser.START_TAG
                            && xrp.getName().equalsIgnoreCase("description")) {
                        xrp.next();
                        String cat = xrp.getText();
                        tempMap.put(SECOND_COLUMN, cat);
                        highScoreHelpList.add(tempMap);
                    }
                }
                eventType = xrp.next();

            }
        } catch (Exception e) {
            Log.d("INFO", e.toString());
        }
    }
}