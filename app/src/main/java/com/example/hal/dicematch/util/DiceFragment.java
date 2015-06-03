package com.example.hal.dicematch.util;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hal.dicematch.R;

import java.io.Serializable;
import java.util.ArrayList;


public class DiceFragment extends Fragment implements Serializable {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int DICES_COUNT = 5;
    final Handler mHandler = new Handler();
    OnFinishRollSelectedListener mListener;
    boolean wait;
    int actualThrow = 0;
    View diceSettings;
    private ArrayList<Dice> dices = new ArrayList<>();
    private ArrayList<Integer> diceValues = new ArrayList<>();
    int diceSize;

    public DiceFragment() {
        // Required empty public constructor
    }

    public void setDiceSize (int diceSize) {
        this.diceSize = diceSize;
    }

    // TODO: Rename and change types and number of parameters
    public static DiceFragment newInstance(String param1, String param2) {
        DiceFragment fragment = new DiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void enterScore(byte[] values) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("INFO", "dices created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initDiceFragment(inflater, container, savedInstanceState);
        return diceSettings;
    }

    public void initDiceFragment (LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState) {

        wait = false;
        diceSettings = inflater.inflate(R.layout.fragment_dice, container, false);
        // Inflate the layout for this fragment
        this.createDice(savedInstanceState);
        this.reorderDices();
        this.saveDiceValues();

        diceSettings.findViewById(R.id.rollButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rollSelectedDice();
                saveDiceValues();
            }
        });


        diceSettings.findViewById(R.id.finishButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("INFO",wait+"");
                if (!wait) {
                    for (int i = 0; i < dices.size(); i++) {
                        dices.get(i).setChecked(false);
                    }
                    ArrayList<Integer> tmp = new ArrayList<Integer>(); //sorted
                    for (int i = 0; i < DICES_COUNT; i++) {
                        tmp.add(dices.get(i).getValue());
                    }
                    mListener.onFinishThrow(tmp);
                    wait = true;
                }
            }
        });
    }

    public ArrayList<Integer> getDiceValues () {
        return this.diceValues;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFinishRollSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    public void createDice(Bundle savedInstanceState) {
        try {
            Dice.setResources(getResources());

            for (int i = 0; i < DICES_COUNT; i++) {
                SquareButton but = (SquareButton) diceSettings.findViewById(getResources().getIdentifier("dice" + (i + 1), "id", getActivity().getPackageName()));
                but.setHeight(diceSize);
                but.setWidth(diceSize);
                final int finalI = i;
                if (savedInstanceState==null) {
                    dices.add(new Dice(but));
                    dices.get(i).roll(true);
                } else {
                    dices.add(new Dice(diceValues.get(i),but));

                }
                dices.get(i).setHandler(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (!wait) {
                            dices.get(finalI).changeChecked();
                        }
                    }
                });
            }
            if (diceValues.size() > 0) {
                this.setDices(diceValues);
                Log.d("INFO", "Loading dices");
            }
        } catch (Exception e) {
            Log.d("INFO", e + " chyba " + e.getLocalizedMessage());
        }
    }

    public void rollAllDice() {
        for (int j = 0; j < 7; j++) {


            final int finalJ = j;
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    for (int i = 0; i < DICES_COUNT; i++) {
                        dices.get(i).roll(true);
                    }

                }
            }, 200 * j);
        }

        mHandler.postDelayed(new Runnable() {
            public void run() {
                reorderDices();
                wait = false;
            }
        }, 1400);
    }

    public void saveDiceValues() {
        Log.d("INFO","Saving dices");
        ArrayList<Integer> diceVals = new ArrayList<>();
        for (int i = 0; i < dices.size(); i++) {
            diceVals.add(dices.get(i).getValue());
        }
        this.diceValues = diceVals;
    }

    public void rollSelectedDice() {
        for (int j = 0; j < 7; j++) {
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    for (int i = 0; i < DICES_COUNT; i++) {
                        dices.get(i).roll();
                    }
                }
            }, 200 * j);
        }
        mHandler.postDelayed(new Runnable() {
            public void run() {
                reorderDices();
                actualThrow++;
                if (actualThrow == 2) {
                    diceSettings.findViewById(R.id.finishButton).performClick();
                }

            }
        }, 1400);
    }

    public void setDices(ArrayList<Integer> diceValues) {
        if (dices.size() == 0) {
            Log.d("INFO", "no dice found");
        }
        for (int i = 0; i < diceValues.size(); i++) {
            dices.get(i).setValue(diceValues.get(i));
        }
        this.reorderDices();
    }

    public void setValues(ArrayList<Integer> diceValues) {
        this.diceValues = diceValues;
    }

    public void reorderDices() {
        for (int i = 0; i < dices.size(); i++) {
            for (int j = 0; j < dices.size() - 1; j++) {
                if (dices.get(j).getValue() > dices.get(j + 1).getValue()) {
                    this.swapDices(j, j + 1);
                }
            }
        }
    }

    public void swapDices(int i, int j) {
        int tmp = dices.get(i).getValue();
        dices.get(i).setValue(dices.get(j).getValue());
        dices.get(j).setValue(tmp);
        if (dices.get(i).getChecked() != dices.get(j).getChecked()) {
            dices.get(i).changeChecked();
            dices.get(j).changeChecked();
        }
    }

    public void setRollNumber(int actualThrow) {
        this.actualThrow = actualThrow;
        if (actualThrow >= 2) {
            diceSettings.findViewById(R.id.finishButton).performClick();
        }
    }

    public int getRollNumber() {
        return actualThrow;
    }

    public interface OnFinishRollSelectedListener {
        void onFinishThrow(ArrayList<Integer> dices);
    }
}
