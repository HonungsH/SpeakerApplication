package com.example.hanneh.speakerapplication;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by hanneh on 3/21/18.
 */

public class ServerRunLocalization {
    static Packet answer_;

    static class connectToServer extends AsyncTask<Void, Void, Void> {
        private String[] ips;

        private static final String MAJS = "hej";

        public connectToServer(String[] ips) {
            this.ips = ips;
        }


        protected Void doInBackground(Void... voids) {
            ServerCommunication communication = new ServerCommunication(MainActivity.host, MainActivity.port);
            Packet p = new Packet();
            p.addHeader((byte)3);
            p.addInt(ips.length);

            for (int i = 0; i < ips.length; i++){
                p.addString(ips[i]);
            }
            p.addInt(0);
            p.finalize();
            communication.send(p);
            answer_ = communication.receive();

            communication.close();

            return null;

        }
    }

    public static Packet run(String[] ips) {
        connectToServer myConnect = new connectToServer(ips);
        myConnect.execute();

        while(myConnect.getStatus() == AsyncTask.Status.RUNNING){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }

        return answer_;

    }
}
