package com.mycompany.organaiser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogChooseColor extends DialogFragment {

    ColorListener colorListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        colorListener = (ColorListener) context;
    }

    int color;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose color");

        color = Color.rgb(2,2,2);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_choose_color, null);

        SeekBar sbRed = view.findViewById(R.id.seek_bar_red);
        SeekBar sbGreen = view.findViewById(R.id.seek_bar_green);
        SeekBar sbBlue = view.findViewById(R.id.seek_bar_blue);

        View paneColor = view.findViewById(R.id.view_color_pane);

        sbRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                color = Color.rgb(calculateColor(sbRed.getProgress()),
                        calculateColor(sbGreen.getProgress()),
                        calculateColor(sbBlue.getProgress()));
                paneColor.setBackgroundColor(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                color = Color.rgb(calculateColor(sbRed.getProgress()),
                        calculateColor(sbGreen.getProgress()),
                        calculateColor(sbBlue.getProgress()));
                paneColor.setBackgroundColor(color);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                color = Color.rgb(calculateColor(sbRed.getProgress()),
                        calculateColor(sbGreen.getProgress()),
                        calculateColor(sbBlue.getProgress()));
                paneColor.setBackgroundColor(color);
            }
        });
        sbGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                color = Color.rgb(calculateColor(sbRed.getProgress()),
                        calculateColor(sbGreen.getProgress()),
                        calculateColor(sbBlue.getProgress()));
                paneColor.setBackgroundColor(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                color = Color.rgb(calculateColor(sbRed.getProgress()),
                        calculateColor(sbGreen.getProgress()),
                        calculateColor(sbBlue.getProgress()));
                paneColor.setBackgroundColor(color);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                color = Color.rgb(calculateColor(sbRed.getProgress()),
                        calculateColor(sbGreen.getProgress()),
                        calculateColor(sbBlue.getProgress()));
                paneColor.setBackgroundColor(color);
            }
        });
        sbBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                color = Color.rgb(calculateColor(sbRed.getProgress()),
                        calculateColor(sbGreen.getProgress()),
                        calculateColor(sbBlue.getProgress()));
                paneColor.setBackgroundColor(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                color = Color.rgb(calculateColor(sbRed.getProgress()),
                        calculateColor(sbGreen.getProgress()),
                        calculateColor(sbBlue.getProgress()));
                paneColor.setBackgroundColor(color);
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                color = Color.rgb(calculateColor(sbRed.getProgress()),
                        calculateColor(sbGreen.getProgress()),
                        calculateColor(sbBlue.getProgress()));
                paneColor.setBackgroundColor(color);
            }
        });

        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                colorListener.onChooseColor(color);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });


        return builder.create();
    }

    private int calculateColor(int progress) {
        int result = (int)(progress*2.5);
        return result;
    }
}
