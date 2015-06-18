package com.example.hal.dicematch.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hal.dicematch.R;
import com.example.hal.dicematch.fragments.StartDialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class FullscreenStartActivity extends FragmentActivity {

    public static final String FIRST_COLUMN = "Name";
    public static final String SECOND_COLUMN = "Score";
    public static final int HIGH_SCORE_SIZE = 10;
    public String lastName = "Name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initUI();
//        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void initUI() {
        setContentView(R.layout.activity_fullscreen_start);
        Button b1 = (Button) findViewById(R.id.exit_button);
        Button b2 = (Button) findViewById(R.id.start_button);
        Button b3 = (Button) findViewById(R.id.load_button);
        Button b4 = (Button) findViewById(R.id.lead_button);


        View.OnClickListener exitHandler = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        View.OnClickListener newGameHandler = new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = StartDialogFragment.newInstance(
                        R.string.alert_dialog_two_buttons_title);
                newFragment.show(getSupportFragmentManager(), "dialog");
            }
        };
        View.OnClickListener loadGameHandler = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), GameActivity.class);
                intent.putExtra("load", "load");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 1);
                }
            }
        };
        View.OnClickListener highScoreHandler = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), HighScoreActivity.class);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        };
        b1.setOnClickListener(exitHandler);
        b2.setOnClickListener(newGameHandler);
        b3.setOnClickListener(loadGameHandler);
        b4.setOnClickListener(highScoreHandler);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("INFO", "restored");
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Method checks whether a new final score enters leaderboard.
     * If so, inserts it into a correct row.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("INFO", "" + resultCode);
        try {
            if (resultCode == RESULT_OK) {
                final String result = data.getStringExtra("result");
                Log.e("INFO", "" + result);
                if (Integer.valueOf(result) > getLowestScore()) {
                    final Dialog dialog = new Dialog(this);
                    dialog.setContentView(R.layout.hc_submit_name);
                    //dialog.setTitle("Title");
                    ((TextView) dialog.findViewById(R.id.score_field)).setText(result);
                    ((EditText) dialog.findViewById(R.id.name_input)).setText(lastName);
                    Button button = (Button) dialog.findViewById(R.id.submit_name);
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            EditText edit = (EditText) dialog.findViewById(R.id.name_input);
                            String text = edit.getText().toString();

                            dialog.dismiss();
                            saveScore(text, result);
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "You didn't make it to the high scores.",
                            Toast.LENGTH_LONG).show();
                }
            } else {

            }
        } catch (Exception e) {
            Log.e("INFO", e.toString());
        }
    }

    /**
     * Method to order dice values and save and obtain a score value for them.
     */
    public void saveScore(String name, String value) {
        try {
            ArrayList<HashMap<String, String>> scores = loadScores();

            if ((scores == null) || (scores.size() == 0)) {
                scores = new ArrayList<>();
                for (int i = 0; i < HIGH_SCORE_SIZE; i++) {
                    HashMap<String, String> temp = new HashMap<>();
                    temp.put(FIRST_COLUMN, "Newbie");
                    temp.put(SECOND_COLUMN, "0");
                    scores.add(temp);
                }
            }

            int[] values = new int[HIGH_SCORE_SIZE];
            for (int i = 0; i < scores.size(); i++) {
                values[i] = Integer.valueOf(scores.get(i).get(SECOND_COLUMN));
            }

            int tmp = 0;
            for (int i = 0; i < HIGH_SCORE_SIZE; i++) {
                if (Integer.valueOf(value) <= values[i]) {
                    tmp++;
                }
            }

            HashMap<String, String> temp = new HashMap<>();
            temp.put(FIRST_COLUMN, name);
            temp.put(SECOND_COLUMN, value);
            scores.add(tmp, temp);
            if (scores.size() > HIGH_SCORE_SIZE) {
                scores.remove(HIGH_SCORE_SIZE);
            }

            File file = new File("high_score.dat");
            HashMap<String, Serializable> data = new HashMap<>();
            data.put("1", scores);
            FileOutputStream fos = openFileOutput(file.toString(), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.flush();
            oos.close();
            fos.close();

        } catch (Exception e) {
            Log.e("INFO", "save score chyba" + e.toString());
        }
    }

    public int getLowestScore() {
        ArrayList<HashMap<String, String>> scores = loadScores();
        if ((scores != null) && (scores.size() > 0)) {
            return Integer.valueOf(scores.get(scores.size() - 1).get(SECOND_COLUMN));
        } else {
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<HashMap<String, String>> loadScores() {
        try {
            File file = new File(this.getFilesDir().getAbsolutePath() + "/high_score.dat");
            if (file.exists()) {
                FileInputStream fos = new FileInputStream(file.toString());
                ObjectInputStream ois = new ObjectInputStream(fos);
                HashMap<String, Serializable> loadedData;
                loadedData = (HashMap<String, Serializable>) ois.readObject();
                ois.close();
                fos.close();
                return (ArrayList<HashMap<String, String>>) loadedData.get("1");
            } else {
                return new ArrayList<>();
            }

        } catch (Exception e) {
            Log.e("INFO", "load scores chyba " + e.toString());
        }
        return new ArrayList<>();
    }

    @Override
    protected void onStop() {
        Log.e("INFO", "savstop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
