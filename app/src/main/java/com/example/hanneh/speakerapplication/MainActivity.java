package com.example.hanneh.speakerapplication;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    private int channelConfiguration = AudioFormat.CHANNEL_IN_MONO, audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    private boolean started = true;

    AudioRecord audioRecord;
    private static final String DEBUG = "DEBUG";
    private static final String MAJS = "YeppYEPP";
    private boolean recording = false;
    Context c;

    MediaPlayer mp_;
    MediaRecorder mr_;
    ImageButton playTestTone, startRecord;
    TextView display;
    Button dataB, majs;

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = (TextView) findViewById(R.id.display);



        Log.e(MainActivity.DEBUG, "OnCreate HAPPENED");

        dataB = findViewById(R.id.data);
        dataB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleMyPancake();
            }
        });

        majs = findViewById(R.id.majs);
        majs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eatSnacksForBreakfast();
                //Log.e(MAJS, "Hej");

            }
        });




        startRecord = (ImageButton) findViewById(R.id.record);
        startRecord.setBackgroundResource(R.drawable.muted);
        startRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playOrStop();
                if (recording) {
                    display.setText("RECORDING");
                    view.setBackgroundResource(R.drawable.microphone);
                } else {
                    display.setText("recording stopped bitch");
                    view.setBackgroundResource(R.drawable.muted);
                }
            }
        });


        playTestTone = (ImageButton) findViewById(R.id.play);
        playTestTone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                playTestTone();
                display.setText("PLAYING TESTTONE.WAV");
            }
        });
    }



        private String createNewAudioFile() {

            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.wav";
        }


        private void playTestTone() {
            mp_ = new MediaPlayer();
            mp_ = MediaPlayer.create(MainActivity.this, R.raw.testtone);


            mp_.start();
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
            }


            Log.e(DEBUG, "RECORDING IN PROGRESS");
        }

        private void stopRecording() {
            Log.e(DEBUG, "RECORDING STOPPED");
            mr_.stop();
            mr_.reset();
            mr_.release();
            mr_ = null;
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

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void handleMyPancake() {

        new NetworkSniffTask(this).execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    static class NetworkSniffTask extends AsyncTask<Void, String, Void> {

        private static final String TAG = "NetWorkSniffTask";


        private WeakReference<Context> mContextRef;
        // Context context;


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



                            Thread t = new Thread(new Runnable() {
                                public void run() {
                                    for (int i = 0; i < 255; i++) {

                                        String testIp = prefix + "." + String.valueOf(i);
                                        InetAddress address = null;
                                        try {
                                            address = InetAddress.getByName(testIp);
                                            if (address.isReachable(1)) {
                                                //publishProgress(testIp);
                                                Log.e(TAG, "HOST: " + "(" + testIp + ") REACHABLE!" + '\n');
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


                }catch (Throwable t) {
                Log.e(TAG, "Well that's not good.", t);
            }

            return null;
            }

        }



        protected void eatSnacksForBreakfast(){
                String s = "No Breakfast is wrong Breakfast";
            new connectToServer().execute();



        }
        class connectToServer extends AsyncTask<Void, Void, Void> {

         private static final String host = "172.25.12.147";
         private static final int port = 10200;



            protected Void doInBackground(Void... voids){
                String in;
                Socket sock;
                DataInputStream inFromServer;
                byte[] bytearray = new byte[1024];
                int data_read;
                try {
                    Log.e(MAJS, "HEREEEEEEEEEEEEEEEEEEEEEEEEEEE");
                    sock = new Socket(host, port);

                    Log.e(MAJS, "CONNECTION ESTABLISHED");


                    inFromServer = new DataInputStream(sock.getInputStream());


                    while(true) {

                        data_read = inFromServer.read(bytearray, 0, bytearray.length);

                        Log.e(MAJS, String.valueOf(data_read));
                        if(data_read < 0) {
                            sock.close();
                            break;
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(MAJS, "COULD NOT CREATE SOCKET");
                   return null;
                }
return null;
            }
        }

        protected boolean openConnection(String host, int port) {

         String in;
         Socket sock;
         BufferedReader inFromServer;
            try {
                sock = new Socket(host, port);
                Log.e(MAJS, "CONNECTION ESTABLISHED");

                while(true) {
                    inFromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    in = inFromServer.readLine();
                    Log.e(MAJS, in);

                    sock.close();
                    break;
                }
                return true;

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(MAJS, "COULD NOT CREATE SOCKET");
                return false;
            }


        }




    }



