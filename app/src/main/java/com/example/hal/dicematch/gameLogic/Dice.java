package com.example.hal.dicematch.gameLogic;

import com.example.hal.dicematch.R;
import com.example.hal.dicematch.util.SquareButton;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;


import java.io.Serializable;


/**
 * Custom class to interpret Dice,
*/
public class Dice implements Serializable {

    private boolean checked;
    private int value;
    private SquareButton diceButton;
    private static Resources res;


    public Dice(Integer val, SquareButton but) {
        this.value = val;
        this.diceButton = but;
        this.checked = false;
    }

    public Dice(SquareButton but) {
        this(((int) Math.floor(Math.random() * 6 + 1)), but);
    }

    /**
     * Obtain resources (backgrounds) from calling activity.
     */
    public static void setResources(Resources res) {
        Dice.res = res;
    }

    /**
     * Re-rolls dice if marked to do so.
     */
    public void roll() {
        if (checked) {
            this.setStatus((int) Math.floor(Math.random() * 6 + 1), true);
        }
    }

    /**
     * Forces re-roll of this dice.
     */
    public void roll(boolean bol) {
        if (bol) {
            this.setStatus((int) Math.floor(Math.random() * 6 + 1), getChecked());
        }
    }

    public boolean getChecked() {
        return this.checked;
    }


    public void setHandler(View.OnClickListener hand) {
        this.diceButton.setOnClickListener(hand);
    }

    /**
     * Check/unchecks dice for re-roll.
     */
    public boolean changeChecked() {
        this.checked = !checked;
        setButtonImage();
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        setButtonImage();
    }

    public int getValue() {
        return this.value;
    }

    /**
     * Method to set value and checked status from outer classes.
     */
    public void setStatus (int val, boolean checked) {
        this.value = val;
        this.checked = checked;
        setButtonImage();
    }



    /**
     * Method to choose correct image and filter.
     */
    public void setButtonImage() {
        setButtonImage (this.value);
    }

    /**
     * Method to choose correct image and filter.
     */
    private void setButtonImage(int val) {
        try {
            if (checked) {
                switch (val) {
                    case 1:
                        this.diceButton.setBackground(res.getDrawable(R.drawable.dice1d));
                        break;
                    case 2:
                        this.diceButton.setBackground(res.getDrawable(R.drawable.dice2d));
                        break;
                    case 3:
                        this.diceButton.setBackground(res.getDrawable(R.drawable.dice3d));
                        break;
                    case 4:
                        this.diceButton.setBackground(res.getDrawable(R.drawable.dice4d));
                        break;
                    case 5:
                        this.diceButton.setBackground(res.getDrawable(R.drawable.dice5d));
                        break;
                    case 6:
                        this.diceButton.setBackground(res.getDrawable(R.drawable.dice6d));
                        break;
                }
                this.diceButton.getBackground().setColorFilter(Color.argb(100, 255, 140, 140), PorterDuff.Mode.DARKEN);
            }else {
                switch (val) {
                    case 1:
                        this.diceButton.setBackground(res.getDrawable(R.drawable.dice1));
                        break;
                    case 2:
                        this.diceButton.setBackground(res.getDrawable(R.drawable.dice2));
                        break;
                    case 3:
                        this.diceButton.setBackground(res.getDrawable(R.drawable.dice3));
                        break;
                    case 4:
                        this.diceButton.setBackground(res.getDrawable(R.drawable.dice4));
                        break;
                    case 5:
                        this.diceButton.setBackground(res.getDrawable(R.drawable.dice5));
                        break;
                    case 6:
                        this.diceButton.setBackground(res.getDrawable(R.drawable.dice6));
                        break;
                }
                this.diceButton.getBackground().setColorFilter(null);
            }
        } catch (Exception e) {
            Log.d("INFO", e.toString());
        }
    }

    /**
     * Debug method to solve checking dice problem.
     */
    public void setCheckedImage(boolean che) {
        if (che) {
            this.diceButton.getBackground().setColorFilter(Color.argb(100, 255, 140, 140), PorterDuff.Mode.DARKEN);
        } else {
            this.diceButton.getBackground().setColorFilter(null);
        }
        Log.d("INFO", "set b " + che);
    }

    public SquareButton getDiceButton () {
        return this.diceButton;
    }

}