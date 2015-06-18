package com.example.hal.dicematch.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.hal.dicematch.R;
import com.example.hal.dicematch.fragments.DiceFragment;
import com.example.hal.dicematch.gameLogic.Game;
import com.example.hal.dicematch.fragments.ScoreFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Activity for the game to happen. Contains fragment to show and perform dice rolls and fragment to submit and show score.
 */
public class GameActivity extends FragmentActivity implements DiceFragment.OnFinishRollSelectedListener, ScoreFragment.OnNextRoundListener {

    public String lastName = "Name";
    int numberOfRounds;
    ScoreFragment scoreFragment;
    DiceFragment diceFragment;
    Game newGame;
    Integer scoreToWrite = 0;
    private Boolean timeToWriteScore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initUI(savedInstanceState);
    }

    public void initUI(Bundle savedInstanceState) {
        timeToWriteScore = false;

        setContentView(R.layout.activity_game);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle extras = getIntent().getExtras();

        if (scoreFragment == null) {
            scoreFragment = new ScoreFragment();
        } else {
        }
        if (diceFragment == null) diceFragment = new DiceFragment();

        getFragmentManager().beginTransaction().add(R.id.scoreWindow, scoreFragment).commit();
        getFragmentManager().beginTransaction().add(R.id.diceWindow, diceFragment).commit();
//        diceFragment.setDiceSize(50);
        if ((extras != null) || (savedInstanceState != null)) {
            Log.d("INFO", "extras exists");
            this.loadGame();
            diceFragment.falseNewGame();
        } else {
            Log.d("INFO", "new game");
            this.numberOfRounds = 16;
            this.newGame = new Game(1, 0);
            Toast.makeText(getApplicationContext(), "Select dice to roll.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFinishThrow(ArrayList<Integer> diceValues) {
        scoreFragment.saveRoll(diceValues);
    }

    @Override
    public boolean writeRow(int row, ArrayList<Integer> dices) {
        if (newGame.getActivePlayer().setScore(row, dices)) {
            this.scoreToWrite = newGame.getActivePlayer().getScore(row);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer newTotalScore() {
        return this.newGame.getActivePlayer().getScore(numberOfRounds - 1);
    }

    @Override
    public boolean getBonus1() {
        return newGame.getActivePlayer().getBonus1Activated();
    }

    @Override
    public int getBonus2() {
        return newGame.getActivePlayer().getBonus2();
    }

    /**
     * Called after the last roll and score entering. Send RESULT_OK with a total score to the calling activity.
     */
    @Override
    public void endGame(String s) {
        Intent returnIntent = new Intent();
        Bundle b =new Bundle();
        b.putString("result", s);
        returnIntent.putExtras(b);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    /**
     * Saves a state of the activity and finishes it. Returning RESULT_CANCELED.
     */
    @Override
    public void onExitGame() {
        this.saveGame();
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }


    /**
     * Method to save GameActivity state into save_game.dat.
     */
    public void saveGame() {
        diceFragment.saveDiceValues();
        File file = new File("save_game.dat");
        HashMap<String, Serializable> data = new HashMap<>();
        data.put("1", diceFragment.getDiceValues());
        data.put("2", numberOfRounds);
        data.put("3", scoreToWrite);
        data.put("4", newGame);
        data.put("5", scoreFragment.getList());
        data.put("6", scoreFragment.getRoundNumber());
        data.put("7", lastName);
        data.put("8", diceFragment.getRollNumber());
        data.put("9", isTimeToWriteScore());
        try {

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


    @Override
    public Integer getScore() {
        return this.scoreToWrite;
    }

    @Override
    public ArrayList<Boolean> getFinished() {
        return newGame.getActivePlayer().getWritableList();
    }

    @Override
    public void onNextRoundThrow() {
        if (scoreFragment.getRoundNumber() == 13) {
            endGame(""+newGame.getActivePlayer().getScore(numberOfRounds-1));
        } else {
            diceFragment.setThrowStatus(0);
            diceFragment.rollAllDice();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        this.saveGame();
    }


    /**
     * Method to load GameActivity state from save_game.dat.
     * If save_game.dat does not exist yes, starts a new game.
     */
    @SuppressWarnings("unchecked")
    void loadGame() {
        try {
            File file = new File(this.getFilesDir().getAbsolutePath() + "/save_game.dat");
            if (file.exists()) {
                FileInputStream fos = new FileInputStream(file.toString());
                ObjectInputStream ois = new ObjectInputStream(fos);
                HashMap<String, Serializable> loadedData;
                loadedData = (HashMap<String, Serializable>) ois.readObject();
                diceFragment.setValues((ArrayList<Integer>) loadedData.get("1"));
                this.numberOfRounds = (int) loadedData.get("2");
                this.scoreToWrite = (int) loadedData.get("3");
                this.newGame = (Game) loadedData.get("4");
                this.scoreFragment.setList((ArrayList<HashMap<String, String>>) loadedData.get("5"));
                this.scoreFragment.refreshScoreFragment(newGame.getActivePlayer(), getResources().getXml(R.xml.categories));
                this.scoreFragment.setRoundNumber((int) loadedData.get("6"));
                this.lastName = (String) loadedData.get("7");
                this.diceFragment.setThrowStatus((int) loadedData.get("8"));
                timeToWriteScore = (Boolean) loadedData.get("9");
                scoreFragment.setValues((ArrayList<Integer>) loadedData.get("1"));
            } else {
                this.numberOfRounds = 16;
                this.newGame = new Game(1, 0);
                Toast.makeText(getApplicationContext(), "Select dice to roll.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("INFO", "loading error " + e.toString());
        }
    }

    public Boolean isTimeToWriteScore() {
        return timeToWriteScore;
    }

    public void setTimeToWriteScore(Boolean timeToWriteScore) {
        this.timeToWriteScore = timeToWriteScore;
        diceFragment.lightFinish(timeToWriteScore);
    }
}