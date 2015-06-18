package com.example.hal.dicematch.gameLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom class to interpret Player logic and score counting logic.
 * Score counting logic will be moved to Game class later.
 */
public class Player implements Serializable{
    ArrayList<Integer> scoreList = new ArrayList<>();
    ArrayList<Boolean> writableList = new ArrayList<>();
    int rowsTotal;
    int actualThrow;
    boolean bonus1Activated = false;
    boolean bonus1Writen = false;
    private int bonus2;
    private boolean bonus2add = false;


    public Player (int rows) {
        this.rowsTotal = rows;
        for (int i = 0; i < rowsTotal; i++) {
            this.scoreList.add(0);
            if ((i==6)||(i==rowsTotal-1)||(i==rowsTotal-3)) {
                this.writableList.add(false);
            } else {
                this.writableList.add(true);
            }
        }
    }

    /**
     * Returns which rows are eligible for score submit.
     */
    public ArrayList<Boolean> getWritableList () {
        return this.writableList;
    }

    /**
     * Returns score value from particular row / category.
     */
    public int getScore (int row) {
        return this.scoreList.get(row);
    }

    /**
     * Set score value to particular row / category if able to do it.
     * @param dices ordered dice values
     */
    public boolean setScore (int position, List<Integer> dices) {
        if (writableList.get(position)) {
            int tmp = this.saveScore(dices, position);
            this.scoreList.set(position, tmp);
            if ((!bonus1Writen) && (!bonus1Activated) && (position <6)) {
                int temp = 0;
                for (int i = 0; i < 6; i++) {
                    temp += scoreList.get(i);
                }
                if (temp>62) {
                    bonus1Activated = true;
                    this.scoreList.set(rowsTotal - 1, (this.scoreList.get(rowsTotal - 1) + 30));
                    this.scoreList.set(6, 30);
                    this.bonus1Writen = true;
                }
            }
            if (bonus2add) {
                this.scoreList.set(13, bonus2*100);
                this.scoreList.set(rowsTotal - 1, (this.scoreList.get(rowsTotal - 1) + 100));
                bonus2add = false;
            }
            this.writableList.set(position, false);
            this.scoreList.set(rowsTotal - 1, (this.scoreList.get(rowsTotal - 1) + tmp));
            this.actualThrow = 0;
            return true;
        } else {
            return false;
        }
    }


    public ArrayList<Integer> getFullScore () {
        return this.scoreList;
    }

    /**
     * Counts score points based on position / category to be writen to.
     * @param dices ordered dice values
     */
    public Integer saveScore(List<Integer> dices, int position) {
        Integer tmp = 0;
        int temp;
        int temp2;
        switch (position) {
            case 0: case 1: case 2:
            case 3: case 4: case 5:
                for (int i = 0; i < dices.size(); i++) {
                    if (dices.get(i) == (position+1)) {
                        tmp+=(position+1);
                    }

                } break;
            case 7:case 8:
                temp=0;
                for (int i = 0; i < dices.size(); i++) {
                    if (dices.get(2).equals(dices.get(i))) {
                        temp++;
                    }
                    tmp += dices.get(i);
                }
                if (temp<(position-4)) {
                    tmp = 0;
                }
                break;
            case 9:
                temp = 0;
                temp2 = 0;
                for (int i = 0; i < dices.size(); i++) {
                    if (dices.get(0).equals(dices.get(i))) {
                        temp++;
                    }
                    if (dices.get(4).equals(dices.get(i))) {
                        temp2++;
                    }
                }
                if (((temp==2)&&(temp2==3))||(temp==3)&&(temp2==2)) {
                    tmp = 20;
                } else {
                    tmp = 0;
                }
                temp=0;
                for (int i = 0; i < dices.size(); i++) {
                    if (dices.get(2).equals(dices.get(i))) {
                        temp++;
                    }
                }
                if (temp == 5) {
                    tmp = 20;
                }
                break;
            case 10: case 11:
                temp = 0;
                temp2 = 0;
                for (int i = 0; i < dices.size()-1; i++) {
                    if (dices.get(i)==dices.get(i+1)-1) {
                        temp++;
                    }
                    if (dices.get(i).equals(dices.get(i+1))) {
                        temp2++;
                    }
                }
                if (temp == 4) {
                    tmp = (position - 10)*10 + 30;
                } else {
                    if (temp == 3) {
                        if (((temp2==1) || ((temp2==0)&&(dices.get(1)-dices.get(0)+dices.get(dices.size()-1)-dices.get(dices.size()-2)>2)))) {
                            tmp = 30-(position - 10)*30;
                        }
                    } else {
                        tmp = 0;
                    }
                }
            case 12:
                temp=0;
                for (int i = 0; i < dices.size(); i++) {
                    if (dices.get(2).equals(dices.get(i))) {
                        temp++;
                    }
                }
                if (temp == 5) {
                    tmp = 50 - (12 - position) * 10;
                }
                break;
            case 14:
                for (int i = 0; i < dices.size(); i++) {
                    tmp+=dices.get(i);
                }
                break;
        }
        temp=0;
        for (int i = 0; i < dices.size(); i++) {
            if (dices.get(2).equals(dices.get(i))) {
                temp++;
            }
        }
        if ((temp == 5)&&(this.getScore(12)!=0)) {
            this.bonus2++;
            bonus2add = true;
        }
        return tmp;
    }

    public boolean getBonus1Activated () {
        return this.bonus1Activated;
    }

    public int getBonus2() {
        return bonus2;
    }
}
