package com.example.hanneh.speakerapplication;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ServerActivity extends BaseMenuActivity {

    Button fabConnect;
    final static String TAG = "debug";
    connectToServer myConnect = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_server);



        fabConnect = findViewById(R.id.fabconnect);


        fabConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                establishConnectionToServer();
                fabConnect.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#30d14d")));
            }
        });





    }



    protected void establishConnectionToServer() {

        String s = "No Breakfast is wrong Breakfast";

        Log.e(TAG, "establish connection");

        if (myConnect == null || myConnect.getStatus() != AsyncTask.Status.RUNNING) {
            Log.e(TAG, "Starting connection");

            myConnect = new connectToServer();
            myConnect.execute();
            Log.e(TAG, myConnect.getStatus().toString());
            return;


        }

        myConnect.cancel(true);
        // Log.e(MAJS, "Debug: DISCONNECTED FROM SERVER");

        myConnect = null;
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
                Log.e(TAG, String.valueOf(packet.getSize()));
                if (isCancelled()){
                    break;
                }
            }

            communication.close();

            return null;

        }
    }



}
