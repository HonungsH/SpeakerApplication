package com.example.hanneh.speakerapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

public class ServerContact extends AsyncTask<Packet, Void, Void> {
    public enum HeaderEnum {
        PACKET_START_LOCALIZATION(1),
        PACKET_CHECK_SPEAKERS_ONLINE(2),
        PACKET_CHECK_SOUND_IMAGE(3),
        PACKET_CHECK_SOUND_IMAGE_WHITE(4),
        PACKET_SET_BEST_EQ(5),
        PACKET_SET_EQ_STATUS(6),
        PACKET_RESET_EVERYTHING(7),
        PACKET_SET_SOUND_EFFECTS(8),
        PACKET_TESTING(9);

        private final int mask;

        HeaderEnum(int mask) {
            this.mask = mask;
        }

        public int getMask() {
            return mask;
        }
    }

    private static String host_ = "localhost";
    private static int port_ = 10200;

    // Answer
    private Packet answer_;

    /*
        AsyncTask
    */

    @Override
    protected Void doInBackground(Packet... packets) {
        Packet sending = packets[0];

        ServerCommunication communication = new ServerCommunication(host_, port_);
        communication.send(sending);

        answer_ = communication.receive();
        communication.close();

        return null;
    }

    /*
        Packets
    */

    public static Packet createLocalization(ArrayList<String> ips) {
        Packet packet = new Packet();
        packet.addHeader((byte)HeaderEnum.PACKET_START_LOCALIZATION.getMask());
        packet.addBool(true);
        packet.addInt(ips.size());

        for (int i = 0; i < ips.size(); i++)
            packet.addString(ips.get(i));

        packet.finalize();
        return packet;
    }

    public static Packet createCalibration(ArrayList<String> speaker_ips, ArrayList<String> mic_ips) {
        Packet packet = new Packet();
        packet.addHeader((byte)HeaderEnum.PACKET_CHECK_SOUND_IMAGE.getMask());
        packet.addBool(false);
        packet.addInt(0); // White noise
        packet.addInt(speaker_ips.size());
        packet.addInt(mic_ips.size());
        packet.addInt(mic_ips.size()); // Gains

        for (int i = 0; i < speaker_ips.size(); i++) {
            packet.addString(speaker_ips.get(i));
        }

        for (int i = 0; i < mic_ips.size(); i++) {
            packet.addString(mic_ips.get(i));
        }

        for (int i = 0; i < mic_ips.size(); i++) {
            packet.addFloat(0);
        }

        packet.finalize();
        return packet;
    }

    /*
        Requests
    */

    public Packet request(Packet packet) {
        Log.e("DEBUG", "Requesting Server...");
        execute(packet);

        Log.e("DEBUG", "Waiting for response...");
        // Waiting for answer from Server
        while (getStatus() == AsyncTask.Status.RUNNING) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }

        Log.e("DEBUG", "Got reponse!");

        // Have answer_
        return answer_;
    }
}
