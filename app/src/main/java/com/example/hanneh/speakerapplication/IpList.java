package com.example.hanneh.speakerapplication;

import java.util.ArrayList;

/**
 * Created by hanneh on 3/20/18.
 */

public final class IpList {
    private static ArrayList<String> speaker_ips_ = new ArrayList<>();
    private static ArrayList<String> mic_ips_ = new ArrayList<>();

    public static void setSpeakerIPs(ArrayList<String> ips) {
        speaker_ips_ = ips;
    }

    public static ArrayList<String> getSpeakerIPs() {
        return speaker_ips_;
    }

    public static void setMicrophoneIPs(ArrayList<String> ips) {
        mic_ips_ = ips;
    }

    public static ArrayList<String> getMicrophoneIPs() {
        return mic_ips_;
    }


}