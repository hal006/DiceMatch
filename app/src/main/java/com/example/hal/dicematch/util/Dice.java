package com.example.hal.dicematch.util;

import com.example.hal.dicematch.GameActivity;
import com.example.hal.dicematch.R;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import java.io.Serializable;

public class Dice  implements Serializable {
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
        this(((int) Math.floor(Math.random() * 6 + 1)), but );
    }

    public static void setResources (Resources res) {
        Dice.res = res;
    }

    public void roll() {
        if (checked) {
            this.setValue((int) Math.floor(Math.random() * 6 + 1));
        }
    }

    public void roll(boolean bol) {
        if (bol) {
            this.setValue((int) Math.floor(Math.random() * 6 + 1));
        }
    }

    public boolean getChecked() {
        return this.checked;
    }



    public void setHandler(View.OnClickListener hand) {
        this.diceButton.setOnClickListener(hand);
    }

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

    public void setValue(int val) {
        this.value = val;
        setButtonImage();
    }

    public SquareButton getDiceButton() {
        return this.diceButton;
    }

    private void setButtonImage() {
        try {

        if (this.checked) {
            switch (value) {
                case 1:
                    diceButton.setBackground(res.getDrawable(R.drawable.dice1c));
                    break;
                case 2:
                    diceButton.setBackground(res.getDrawable(R.drawable.dice2c));
                    break;
                case 3:
                    diceButton.setBackground(res.getDrawable(R.drawable.dice3c));
                    break;
                case 4:
                    diceButton.setBackground(res.getDrawable(R.drawable.dice4c));
                    break;
                case 5:
                    diceButton.setBackground(res.getDrawable(R.drawable.dice5c));
                    break;
                case 6:
                    diceButton.setBackground(res.getDrawable(R.drawable.dice6c));
                    break;
            }
        } else {
            switch (value) {
                case 1:
                    diceButton.setBackground(res.getDrawable(R.drawable.dice1));
                    break;
                case 2:
                    diceButton.setBackground(res.getDrawable(R.drawable.dice2));
                    break;
                case 3:
                    diceButton.setBackground(res.getDrawable(R.drawable.dice3));
                    break;
                case 4:
                    diceButton.setBackground(res.getDrawable(R.drawable.dice4));
                    break;
                case 5:
                    diceButton.setBackground(res.getDrawable(R.drawable.dice5));
                    break;
                case 6:
                    diceButton.setBackground(res.getDrawable(R.drawable.dice6));
                    break;
            }
        }

        } catch (Exception e) {
            Log.d("INFO",e.toString());
        }
    }
}