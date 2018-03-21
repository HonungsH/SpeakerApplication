package com.example.hanneh.speakerapplication;

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
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    Context c;

    MediaPlayer mp_;
    MediaRecorder mr_;
    ImageButton playTestTone, startRecord;
    TextView display, timer;
    Button dataB, majs, mapB, mip;
    connectToServer myConnect = null;
    ProgressBar recordingProgressBar;
    int progress = 0;
    int progressValue;
    int seconds, minute, hour;
    Timer t;
    Handler handler = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);

        recordingProgressBar = findViewById(R.id.progressBar2);

        recordingProgressBar.setVisibility(View.GONE);
        recordingProgressBar.getIndeterminateDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);

        timer = findViewById(R.id.timer);

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

        dataB = findViewById(R.id.data);
        dataB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                printAllIpInNetwork();
            }
        });

        majs = findViewById(R.id.majs);
        majs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                establishConnectionToServer();


            }
        });




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
                    display.setText("recording stopped bitch");
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


                /*
                if (myConnect.getStatus() == AsyncTask.Status.RUNNING){
                    myConnect.cancel(true);
                    Log.e(MAJS, "CONNECTION STOPPED");

                }else {
                    myConnect = new connectToServer();
                    myConnect.execute();
                }
                */
        }
        class connectToServer extends AsyncTask<Void, Void, Void> {

         private static final String host = "172.25.12.147";
         private static final int port = 10200;


            protected Void doInBackground(Void... voids) {
                ServerCommunication communication = new ServerCommunication(host, port);
                Packet hello = new Packet();
                hello.addHeader((byte)0x00);
                hello.addString("Hannebajs");
                hello.finalize();
                while(true){
                    communication.send(hello);
                    SystemClock.sleep(2000);
                    Packet packet = communication.receive();
                    Log.e(MAJS, String.valueOf(packet.getSize()));
                    if (isCancelled()){
                        break;
                    }
                }





                /*
                Socket sock;
                DataInputStream inFromServer;
                byte[] bytearray = new byte[1024];
                int data_read;
                try {
                    Log.e(MAJS, "We Are HERE");
                    sock = new Socket(host, port);

                    Log.e(MAJS, "CONNECTION ESTABLISHED/SOCKET CREATED");


                    inFromServer = new DataInputStream(sock.getInputStream());


                    while(true) {

                        data_read = inFromServer.read(bytearray, 0, bytearray.length);

                        Log.e(MAJS, String.valueOf(data_read));
                        if(data_read < 0) {

                            sock.close();
                            break;
                        }
                        if (isCancelled()) {
                            sock.close();
                            return null;
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(MAJS, "COULD NOT CREATE SOCKET");
                   return null;
                }


*/
                communication.close();

                return null;

            }
        }






    }



