package com.example.hanneh.speakerapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v7.widget.CardView;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity  {

    private int channelConfiguration = AudioFormat.CHANNEL_IN_MONO, audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    private boolean started = true;

    AudioRecord audioRecord;
    private static final String DEBUG = "DEBUG";
    private static final String MAJS = "OUR_DEBUG";
    private boolean recording = false;
    public static final String host = "172.25.12.147";
    public static final int port = 10200;
    Context c;
    int minHeight;
    private Animation animationUp;
    private Animation animationDown;
    MediaPlayer mp_;
    MediaRecorder mr_;
    ImageButton playTestTone, startRecord;
    TextView display, timer;
    CardView dataB, connectServer, mapB, mip, eq;
  //  connectToServer myConnect = null;
    ProgressBar recordingProgressBar;
    int progress = 0;
    int progressValue;
    int seconds, minute, hour;
    Timer t;
    ImageView imView;
    Handler handler = new Handler();


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {





        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        hideSystemUI();





        // display = findViewById(R.id.display);

     /*   recordingProgressBar = findViewById(R.id.progressBar2);

        recordingProgressBar.setVisibility(View.GONE);
        recordingProgressBar.getIndeterminateDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
*/
        // timer = findViewById(R.id.timer);



        connectServer = findViewById(R.id.connect);
        connectServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ServerActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });


        mip = findViewById(R.id.ip);
        mip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newIntent = new Intent(MainActivity.this, RecyclerViewActivity.class);
                MainActivity.this.startActivity(newIntent);
            }
        });


        mapB = findViewById(R.id.map);
        mapB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });


        Log.e(MainActivity.DEBUG, "OnCreate HAPPENED");




        eq = findViewById(R.id.equalizer);
        eq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, EqActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

/*

        startRecord = findViewById(R.id.record);
        startRecord.setBackgroundResource(R.drawable.muted);
        startRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playOrStop();
                if (recording) {
                    display.setText("RECORDING");
                    view.setBackgroundResource(R.drawable.microphone);
                    recordingProgressBar.setVisibility(view.VISIBLE);


                } else {
                    display.setText("Recording stopped");
                    view.setBackgroundResource(R.drawable.muted);
                    recordingProgressBar.setVisibility(view.GONE);
                }
            }
        });



        playTestTone = findViewById(R.id.play);
        playTestTone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                playTestTone();
                display.setText("PLAYING TESTTONE.WAV");
            }
        });


    }



    private void setProgressValue(final int progress) {

        // set the progress
        recordingProgressBar.setProgress(progress);
        // thread is used to change the progress value
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setProgressValue(progress + 1);
            }
        });
        thread.start();
    }



        private String createNewAudioFile() {

            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.wav";
        }


        private void playTestTone() {
            mp_ = new MediaPlayer();
            mp_ = MediaPlayer.create(MainActivity.this, R.raw.testtone);


            mp_.start();
        }

    private void playOrStop() {

        if (recording) {
            recording = false;
            stopRecording();
        } else {
            recording = true;
            startRecording();
        }
    }

        public void startRecording() {

            if (mr_ == null) {
                String fileString = createNewAudioFile();
                File file = new File(fileString);
                Log.e(DEBUG, "FILEPATH" + fileString);

                try {
                    file.createNewFile();
                } catch (IOException e) {
                    Log.e(DEBUG, "Could not create file " + fileString, e);
                }

                mr_ = new MediaRecorder();

                mr_.setAudioSource(MediaRecorder.AudioSource.MIC);
                mr_.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mr_.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mr_.setOutputFile(fileString);

                try {
                    mr_.prepare();
                    mr_.start();

                } catch (IOException e) {
                    Log.e(DEBUG, "prepare() failed", e);
                }

                t = new Timer("RecordTime", true);
                minute = 0;
                seconds = 0;
                t.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        timer.post(new Runnable() {

                            public void run() {
                                seconds++;
                                if (seconds == 60) {
                                    seconds = 0;
                                    minute++;
                                }

                                timer.setText(
                                        (minute > 9 ? minute : ("0" + minute))
                                        + ":"
                                        + (seconds > 9 ? seconds : "0" + seconds));


                            }
                        });

                    }
                }, 1000, 1000);


                setProgressValue(progress);
            }


            Log.e(DEBUG, "RECORDING IN PROGRESS");
        }

        private void stopRecording() {
            Log.e(DEBUG, "RECORDING STOPPED");
            t.cancel();
            timer.setText("00" + ":" + "00");
            mr_.stop();
            mr_.reset();
            mr_.release();
            mr_ = null;
        }

*/
    }

    public void onResume(){
        super.onResume();
        hideSystemUI();
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void printAllIpInNetwork() {

        new NetworkSniffTask(this).execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    static class NetworkSniffTask extends AsyncTask<Void, String, Void> {

        private static final String TAG = "NetWorkSniffTask";

        private WeakReference<Context> mContextRef;

        public NetworkSniffTask(Context context) {
            mContextRef = new WeakReference<Context>(context);
        }


        @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "Let's sniff the network");


            try {

                Context context = mContextRef.get();

                if (context != null) {

                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                    WifiInfo connectionInfo = wm.getConnectionInfo();
                    int ipAddress = connectionInfo.getIpAddress();
                    String ipString = Formatter.formatIpAddress(ipAddress);


                    Log.e(TAG, "ACTIVE: " + String.valueOf(activeNetwork));
                    Log.e(TAG, "IP: " + String.valueOf(ipString));

                    //String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                    String prefix_base = ipString.substring(0, ipString.indexOf(".", ipString.indexOf(".") + 1)) + ".";
                    //172.25.


                    Log.e(TAG, "prefixstart: " + prefix_base);


                    for (int k = 0; k < 255; k++) {
                       final String prefix = prefix_base + String.valueOf(k);
                      // Log.e(MAJS, "ARE WE HERE");
                            Thread t = new Thread(new Runnable() {

                                public void run() {

                                    for (int i = 0; i < 255; i++) {
                                      //Log.e(MAJS, "MJAAAAAAAAAAAAAUUUUUUUUUUUUUU");

                                        String testIp = prefix + "." + String.valueOf(i);
                                        InetAddress address = null;
                                        try {
                                            address = InetAddress.getByName(testIp);
                                            if (address.isReachable(500)) {
                                                //publishProgress(testIp);
                                                Log.e(MAJS, "HOST: " + "(" + testIp + ") REACHABLE!" + '\n');
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }

                            });
                            t.start();
                        }
                    }


                } catch (Throwable t) {
                Log.e(MAJS, "SOMETHING WENT WRONG", t);
            }

            return null;
            }

        }




        /*
        protected void establishConnectionToServer(){

                String s = "No Breakfast is wrong Breakfast";

                Log.e(MAJS, "establish connection");

                if (myConnect == null || myConnect.getStatus() != AsyncTask.Status.RUNNING) {
                    Log.e(MAJS, "Starting connection");

                    myConnect = new connectToServer();
                    myConnect.execute();
                    Log.e(MAJS, myConnect.getStatus().toString());
                    return;


                }

                myConnect.cancel(true);
               // Log.e(MAJS, "Debug: DISCONNECTED FROM SERVER");

                myConnect = null;

                // It's already running



                if (myConnect.getStatus() == AsyncTask.Status.RUNNING){
                    myConnect.cancel(true);
                    Log.e(MAJS, "CONNECTION STOPPED");

                }else {
                    myConnect = new connectToServer();
                    myConnect.execute();
                }

        }
        */



    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().getRootView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }



    }



