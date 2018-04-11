package com.example.hanneh.speakerapplication;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import me.ithebk.barchart.BarChart;
import me.ithebk.barchart.BarChartModel;
import me.ithebk.barchart.BarChart;
import me.ithebk.barchart.BarChartModel;

import static android.content.ContentValues.TAG;


public class EqualizerTab1 extends Fragment {
    TextView text, score_text;

    static ImageView mImageView;

    static View rootView;
    EditText editText;
    Editable numberOfIterations;

    FloatingActionButton fab, fabstop, setBestEq, fabreset;

    AlertDialog.Builder builder;
    HorizontalScrollView hz;
    List<BarChartModel> barChartModelList;
    List<BarChartModel> barChartFrequencyBands;
    int[] frequencyBands;

    public EqualizerTab1() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle savedstate) {

        rootView = inflater.inflate(R.layout.fragment_tab1, group, false);

        updateStartEq();

        //Create a list with the Barchartmodels
        barChartModelList = new ArrayList<>();
        barChartFrequencyBands = new ArrayList<>();
        frequencyBands = new int[] {63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000};

        BarChart barChartUpper = (BarChart) rootView.findViewById(R.id.bar_chart_vertical);
        barChartUpper.setBarMaxValue(100);

        BarChart barChartLower = rootView.findViewById(R.id.bar_chart_bands);
        barChartLower.setBarMaxValue(10);

        //for loop for adding upper bars
        for (int i = 1; i < 10; i++){
            BarChartModel barChar = new BarChartModel();
            barChar.setBarValue(70);
            //   barChar.setBarColor(Color.parseColor("#070707"));
            barChar.setBarTag(null); //You can set your own tag to bar model
            barChar.setBarText(String.valueOf(i) + "dB");
            barChartModelList.add(barChar);
        }


        //Add all the created bars to the list
        barChartUpper.addBar(barChartModelList);

        //for loop for adding lower bars
        for (int j = 1; j < 10; j++){
            BarChartModel barChar2 = new BarChartModel();
            barChar2.setBarValue(0);
            barChar2.setBarColor(Color.parseColor("#ffffff"));

            if(frequencyBands[j-1] >= 1000){
                String band = String.valueOf(frequencyBands[j-1]);
                band = band.replaceAll("000", "K");
                barChar2.setBarText(band + '\n' + "Hz");
            }
            else{
                barChar2.setBarText(String.valueOf(frequencyBands[j-1]) + '\n' +"Hz");

            }
            barChartFrequencyBands.add(barChar2);

        }




        barChartLower.addBar(barChartFrequencyBands);














        text = rootView.findViewById(R.id.section_label);
        text.setText("Equalizer Values before start");


        score_text = rootView.findViewById(R.id.score_text);

        hz = rootView.findViewById(R.id.horizontalbar);
      //  hz.setBackgroundDrawable(getResources().getDrawable((R.drawable.borderhorizont)));
        hz.setBackgroundColor(Color.TRANSPARENT);

        hz.setScrollbarFadingEnabled(false);

        hideSystemUI();



        fabstop = rootView.findViewById(R.id.fabstop);

        fabstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add method to stop iterations.
            }
        });

        Log.e(TAG, "ONCREATE");


        // onDraw(mCanvas);

        builder = new AlertDialog.Builder(getActivity());

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Number of iterations");


                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.alert_dialog, null);

                builder.setView(dialoglayout);
                editText = dialoglayout.findViewById(R.id.editText);
                numberOfIterations = editText.getText();


                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("Run", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Toast.makeText(EqActivity.this, "Number: " + numberOfIterations.toString(), Toast.LENGTH_LONG).show();

                        Snackbar.make(view, "Optimizing EQ values in " + numberOfIterations.toString() + " iterations. Please wait for the process to finish.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        view.setBackgroundResource(R.drawable.pause);  //Fungerar ej?
                        hideSystemUI();
                        ViewPager pager = getActivity().findViewById(R.id.container);
                        pager.setCurrentItem(1);


                    }
                });






                builder.show();


            }

        });


        setBestEq = rootView.findViewById(R.id.setBestEq);
        setBestEq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int eqValue = setBestEq();
                text.setText("New best EQ set with score: " + eqValue);
            }
        });

        fabreset = rootView.findViewById(R.id.fabreset);
        fabreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resetSystem()) {
                    text.setText("RESET SUCCESSFULL. ALL EQ VALUES LOST");
                }
            }
        });


        return rootView;
    }

    private boolean resetSystem() {
        return true;
    }

    private int setBestEq() {
        return 1;
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
       rootView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    //Updates fragment 1 with EQ values
    public void updateStartEq(){

}
}