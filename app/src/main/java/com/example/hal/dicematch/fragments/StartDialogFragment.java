package com.example.hal.dicematch.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.hal.dicematch.activities.GameActivity;
import com.example.hal.dicematch.R;

/**
 * Dialog to choose the number of human and AI players;
 */
public class StartDialogFragment extends DialogFragment {

    private static final String INFO = "INFO";
    private static final String ERROR = "ERROR";
    private static final String RESULT = "RESULT";
    static final int EXTRAS_VAL = 1;

    public StartDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static StartDialogFragment newInstance(int title) {
        StartDialogFragment frag = new StartDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        final View startSettings = inflater.inflate(R.layout.fragment_start_dialog, container, false);

        SeekBar bar1 = (SeekBar) startSettings.findViewById(R.id.plCountBar);
        bar1.setProgress(0);
        bar1.setEnabled(false);
        ((TextView) (startSettings.findViewById(R.id.noPlay))).setText("Human Players: " + 1);
        bar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            //@Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean fromTouch) {
                ((TextView) (startSettings.findViewById(R.id.noPlay))).setText("Human Players: " + String.valueOf(progress + 1));
            }

            //@Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            //@Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }
        });

        SeekBar bar2 = (SeekBar) startSettings.findViewById(R.id.compCountBar);
        bar2.setProgress(0);
        bar2.setEnabled(false);

        ((TextView) (startSettings.findViewById(R.id.noComp))).setText("AI Players: " + 0);

        bar2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            //@Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean fromTouch) {
                ((TextView) (startSettings.findViewById(R.id.noComp))).setText("AI Players: " + String.valueOf(progress - 1));
            }

            //@Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            //@Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }
        });
        Button b1 = (Button) startSettings.findViewById(R.id.startButton);
        Button b2 = (Button) startSettings.findViewById(R.id.cancelButton);
        View.OnClickListener cancelHandler = new View.OnClickListener() {
            public void onClick(View v) {
                getDialog().dismiss();
            }
        };

        View.OnClickListener startGameHandler = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), GameActivity.class);
                startActivityForResult(intent, EXTRAS_VAL);
                getDialog().dismiss();
            }
        };
        b1.setOnClickListener(startGameHandler);
        b2.setOnClickListener(cancelHandler);
        return startSettings;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(INFO, "fra" + data.getStringExtra("result"));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(INFO, "fragment canceled");
            }
        }
    }

    /**
     * The system calls this only when creating the layout in a dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("Settings");
        return dialog;
    }
}
