package com.example.hal.dicematch;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hal.dicematch.util.StartDialogFragment;
import com.example.hal.dicematch.util.SystemUiHider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenStartActivity extends FragmentActivity {

    private static final String INFO = "INFO";
    private static final String ERROR = "ERROR";
    private static final String RESULT = "RESULT";

    public static final String FIRST_COLUMN="Name";
    public static final String SECOND_COLUMN = "Score";
    public static final int HIGH_SCORE_SIZE = 10;
    public String lastName = "Name";

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen_start);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        //final View contentView = findViewById(R.id.fullscreen_content);

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
//        b3.setOnClickListener(myhandler3);
        b4.setOnClickListener(highScoreHandler);
        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
//        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
//        mSystemUiHider.setup();
//        mSystemUiHider
//                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
//                    // Cached values.
//                    int mControlsHeight;
//                    int mShortAnimTime;
//
//                    @Override
//                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//                    public void onVisibilityChange(boolean visible) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//                            // If the ViewPropertyAnimator API is available
//                            // (Honeycomb MR2 and later), use it to animate the
//                            // in-layout UI controls at the bottom of the
//                            // screen.
//                            if (mControlsHeight == 0) {
//                                mControlsHeight = controlsView.getHeight();
//                            }
//                            if (mShortAnimTime == 0) {
//                                mShortAnimTime = getResources().getInteger(
//                                        android.R.integer.config_shortAnimTime);
//                            }
//                            controlsView.animate()
//                                    .translationY(visible ? 0 : mControlsHeight)
//                                    .setDuration(mShortAnimTime);
//                        } else {
//                            // If the ViewPropertyAnimator APIs aren't
//                            // available, simply show or hide the in-layout UI
//                            // controls.
//                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
//                        }
//
//                        if (visible && AUTO_HIDE) {
//                            // Schedule a hide().
//                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
//                        }
//                    }
//                });

        // Set up the user interaction to manually show or hide the system UI.
//        contentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (TOGGLE_ON_CLICK) {
//                    mSystemUiHider.toggle();
//                } else {
//                    mSystemUiHider.show();
//                }
//            }
//        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        // findViewById(R.id.exit_button).setOnTouchListener(mDelayHideTouchListener);
    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//
//        // Trigger the initial hide() shortly after the activity has been
//        // created, to briefly hint to the user that UI controls
//        // are available.
//        delayedHide(100);
//    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
        final String result = data.getStringExtra("result");

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

    }

    public void saveScore (String name, String value) {
        try {
            ArrayList<HashMap<String, String>> scores = loadScores();

            if ((scores == null)||(scores.size()==0)) {
                scores = new ArrayList<HashMap<String, String>>();
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
            if (scores.size()>HIGH_SCORE_SIZE) {
                scores.remove(HIGH_SCORE_SIZE);
            }

            File file = new File("high_scores.dat");
            HashMap<String, Serializable> data = new HashMap<>();
            data.put("1", scores);
            FileOutputStream fos = openFileOutput(file.toString(), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.flush();
            oos.close();
            fos.close();

        } catch (Exception e) {
            Log.d("INFO", e.toString());
        }
    }

    public int getLowestScore () {
        ArrayList<HashMap<String, String>> scores = loadScores();
        if ((scores!=null)&&(scores.size()>0)) {
            return Integer.valueOf(scores.get(scores.size() - 1).get(SECOND_COLUMN));
        } else {
            return 0;
        }
    }

    public ArrayList<HashMap<String, String>> loadScores() {
        try {
            File file = new File(this.getFilesDir().getAbsolutePath() + "/high_scores.dat");
            Log.d("INFO", "highscores object loaded");
            if (file.exists()) {
                Log.d("INFO", "highscores object loaded");
                FileInputStream fos = new FileInputStream(file.toString());
                ObjectInputStream ois = new ObjectInputStream(fos);
                HashMap<String, Serializable> loadedData;
                loadedData = (HashMap<String, Serializable>) ois.readObject();
                Log.d("INFO", "highscores object loaded");
                return (ArrayList<HashMap<String, String>>) loadedData.get("1");
            } else {
                Log.d("INFO", "highscores objectdsdadds loaded");
                return new ArrayList<HashMap<String, String>>();
            }

        } catch (Exception e) {
            Log.d("INFO", e.toString());
        }
        return new ArrayList<HashMap<String, String>>();
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    @Override
    protected void onStop () {
        super.onStop();
    }
    @Override
    protected void onDestroy () {
        super.onDestroy();
    }
    public void doPositiveClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
    }

    public void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");
    }
}
