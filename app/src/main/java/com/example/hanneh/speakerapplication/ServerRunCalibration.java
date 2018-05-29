package com.example.hanneh.speakerapplication;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static com.example.hanneh.speakerapplication.ServerActivity.TAG;


/**
 * Created by hanneh on 3/21/18.
 */

public class ServerRunCalibration {
    static Packet answer_;

    static class connectToServer extends AsyncTask<Void, Void, Void> {
        private String[] ips;
        private String[] mics_ip;

        private static final String MAJS = "hej";

        public connectToServer(String[] ips, String[] mics_ip) {
            this.ips = ips;
            this.mics_ip = mics_ip;
        }


        protected Void doInBackground(Void... voids) {
            ServerCommunication communication = new ServerCommunication(MainActivity.host, MainActivity.port);
            Packet p = new Packet();
            p.addHeader((byte) 1);
            p.addBool(true);
            p.addInt(ips.length);

            for (int i = 0; i < ips.length; i++) {
                p.addString(ips[i]);
            }
            //p.addInt(0);
            p.finalize();
            communication.send(p);
            answer_ = communication.receive();

            communication.close();

            return null;

        }
    }

    public static Packet run(String[] ips, String[] mics) {
        connectToServer myConnect = new connectToServer(ips, mics);
        myConnect.execute();

        while (myConnect.getStatus() == AsyncTask.Status.RUNNING) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

        return answer_;

    }

    public static void parseFromFile() {
        Log.e(TAG, "does this work");
        ArrayList<String> string_ips = new ArrayList<>();
        ArrayList<String> mic_ips = new ArrayList<>();

        try {

            File f_speaker = new File("/storage/emulated/0/" + "speaker_config");
            Scanner scan = new Scanner(f_speaker);

            File f_mic = new File("/storage/emulated/0/" + "speaker_config");
            Scanner scan2 = new Scanner(f_mic);


            while (scan.hasNextLine()){
                //Log.e(TAG, scan.nextLine());
                string_ips.add(scan.nextLine());

                // System.out.println(scan.nextLine());
            }

            while(scan2.hasNextLine()){
                mic_ips.add(scan2.nextLine());
            }



            //  BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open(("mic_ips")));
            // String line;
            //Log.e(TAG, "did i get here");
            //while ((line = br.readLine()) != null) {
/*

                string_ips.add(line);
            }
            for (int i = 0; i < string_ips.size(); i++){
                Log.e(TAG, string_ips.get(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    */
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
