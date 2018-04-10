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
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;


public class EqualizerTab1 extends Fragment {
    TextView text;

    static ImageView mImageView;

    static View rootView;
    EditText editText;
    Editable numberOfIterations;

    FloatingActionButton fab, fabstop, setBestEq;

    AlertDialog.Builder builder;

    public EqualizerTab1() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle savedstate) {

        rootView = inflater.inflate(R.layout.fragment_tab1, group, false);

        text = rootView.findViewById(R.id.section_label);
        text.setText("is this different");







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


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

                    }
                });


                builder.setPositiveButton("Run till stop", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Här kör vi
                    }
                });

                builder.show();


            }

        });


        return rootView;
    }


}