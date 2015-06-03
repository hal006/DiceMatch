package com.example.hal.dicematch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.hal.dicematch.util.DiceFragment;
import com.example.hal.dicematch.util.Game;
import com.example.hal.dicematch.util.Player;
import com.example.hal.dicematch.util.ScoreFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class GameActivity extends FragmentActivity implements DiceFragment.OnFinishRollSelectedListener, ScoreFragment.OnNextRoundListener {

    int numberOfRounds;
    ScoreFragment scoreFragment;
    DiceFragment diceFragment;
    Game newGame;
    Integer scoreToWrite = 0;
    public String lastName = "Name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initUI(savedInstanceState);
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
    public void onExitGame() {
        this.saveGame();
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void sendResult(final String result) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",result);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void saveGame () {
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
        diceFragment.setRollNumber(0);
        diceFragment.rollAllDice();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        Log.d("INFO", "save instance activity");
        this.saveGame();
    }

        public void initUI(Bundle savedInstanceState) {

            setContentView(R.layout.activity_game);
            //Remove notification bar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Bundle extras = getIntent().getExtras();

            if (scoreFragment == null) {
                scoreFragment = new ScoreFragment();
                Log.d("INFO", "sc created");
            } else {
                Log.d("INFO", "sc exists");
            }
            if (diceFragment == null) diceFragment = new DiceFragment();



            Log.d("INFO", "adding fragments");
            getFragmentManager().beginTransaction().add(R.id.scoreWindow, scoreFragment).commit();
        Log.d("INFO", "adding dice fragments");
            getFragmentManager().beginTransaction().add(R.id.diceWindow, diceFragment).commit();
            // Create a new Fragment to be placed in the activity layout
        diceFragment.setDiceSize(50);
            Log.d("INFO", "added fragments");
            //scoreFragment.setArguments(getIntent().getExtras());
        if ((extras != null)||(savedInstanceState != null)) {
            Log.d("INFO", "extras exists");
            this.loadGame();
        } else {
            Log.d("INFO", "new game");
            this.numberOfRounds = 16;
            this.newGame = new Game(1, 0);

        }
            Player firstPlayer = new Player(numberOfRounds);
    }

    void loadGame() {
        try {
            File file = new File(this.getFilesDir().getAbsolutePath() + "/save_game.dat");
            FileInputStream fos = new FileInputStream(file.toString());
            ObjectInputStream ois = new ObjectInputStream(fos);
            HashMap<String, Serializable> loadedData;
            loadedData = (HashMap<String, Serializable>) ois.readObject();
            Log.d("INFO", "setting dice values");
            diceFragment.setValues((ArrayList<Integer>) loadedData.get("1"));
            this.numberOfRounds = (int) loadedData.get("2");
            this.scoreToWrite = (int) loadedData.get("3");
            this.newGame = (Game) loadedData.get("4");
            this.scoreFragment.refreshScoreFragment(newGame.getActivePlayer());
            this.scoreFragment.setRoundNumber((int) loadedData.get("6"));
            this.lastName = (String)loadedData.get("7");
            this.diceFragment.setRollNumber((int)loadedData.get("8"));
            Log.d("INFO", ""+loadedData.get("8"));
        } catch (Exception e) {
            Log.d("INFO", e.toString());
        }
    }
}