package com.example.hal.dicematch.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * This is used to make square buttons.
 * @see Button
 */
public class SquareButton extends Button {

    public SquareButton(Context context) {
        super(context);
    }

    public SquareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }
}