package com.example.hanneh.speakerapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SpeakerActivity extends BaseMenuActivity {
    private static final String TAG = "debug";
    private static final int CAPTURE_TAG = 0;
    private static final int VOLUME_TAG = 1;
    private String ip_;
    private ToggleButton toggle;
    private ProgressBar pbar;
    TextView txt, percentVolume, percentCapture;
    SeekBar volumeSeekbar, captureSeekbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);

        Intent i = getIntent();
        String ip = i.getExtras().getString("ip");
        ip_ = ip;
        Log.e(TAG, ip);

        txt = findViewById(R.id.txt);
        percentVolume = findViewById(R.id.percent);
        percentCapture = findViewById(R.id.percent2);
        txt.setText(ip);

        volumeSeekbar = (SeekBar)findViewById(R.id.seekBar);
        volumeSeekbar.setMax(63);

        captureSeekbar = findViewById(R.id.captureSeekbar);
        captureSeekbar.setMax(63);

        updatePercentValue(MyAdapter.speakers.getVolume(ip), 1);
        volumeSeekbar.setProgress(MyAdapter.speakers.getVolume(ip));

        updatePercentValue(MyAdapter.speakers.getCapture(ip), 0);
        captureSeekbar.setProgress(MyAdapter.speakers.getCapture(ip));

        toggle = findViewById(R.id.toggleButton);
        pbar = findViewById(R.id.pbar);
        pbar.setVisibility(View.GONE);



        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (toggle.isChecked()){
                   pbar.setVisibility(View.VISIBLE);
                   pbar.getIndeterminateDrawable().setColorFilter(
                           Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
                   Toast.makeText(getApplicationContext(), "Speaker with ip: " + ip_ + " is currently playing", Toast.LENGTH_LONG).show();
               }
               else     {
                   pbar.setVisibility(view.GONE);
               }
            }
        });

        toggle.setTextOn("ON");
        toggle.setTextOff("OFF");
        controlVolume();
        controlCapture();
    }



    private void controlCapture() {
        final String new_ip = ip_;
        try {
            captureSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    captureSeekbar.setProgress(progress);
                    MyAdapter.speakers.setCapture(new_ip, progress);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
        captureSeekbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    int currlvl = captureSeekbar.getProgress();
                    captureSeekbar.setProgress(currlvl);
                    MyAdapter.speakers.setCapture(new_ip, currlvl);
                    updatePercentValue(currlvl, 0);
                }
                return false;
            }
        });
    }


    private void controlVolume()
        {
            final String this_ip = ip_;

            try
            {
                volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onStopTrackingTouch(SeekBar arg0)
                    {
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar arg0)
                    {
                    }
                    @Override
                    public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser)
                    {
                        volumeSeekbar.setProgress(progress);
                        MyAdapter.speakers.setVolume(this_ip, progress);
                    }
                });

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            volumeSeekbar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        int CurrentLevel = volumeSeekbar.getProgress();
                        volumeSeekbar.setProgress(CurrentLevel);
                        MyAdapter.speakers.setVolume(this_ip, CurrentLevel);
                        updatePercentValue(CurrentLevel, 1);

                    }

                    return false;
                }
            });

        }

    private void updatePercentValue(int progress, int tag) {
        if (tag == 0) {
            percentCapture.setText("Capture: " + progress);
        }else
            percentVolume.setText("Volume: " + progress);
    }
}



