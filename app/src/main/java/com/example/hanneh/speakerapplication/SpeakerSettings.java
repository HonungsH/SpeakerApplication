package com.example.hanneh.speakerapplication;

import java.util.ArrayList;

public class SpeakerSettings {
    private class Speaker {
        public String ip_;
        public int volume_;
        public int capture_;

        public Speaker(String ip, int volume, int capture) {
            ip_ = ip;
            volume_ = volume;
            capture_ = capture;
        }
    }

    public ArrayList<Speaker> speakers;

    public SpeakerSettings(){

        speakers = new ArrayList<>();
    }

    public void addSpeaker(String ip) {
        Speaker speaker = new Speaker(ip, 0, 0);

        speakers.add(speaker);
    }

    public void setVolume(String ip, int volume) {
        Speaker speaker = findSpeaker(ip);

        speaker.volume_ = volume;
    }

    public int getVolume(String ip) {
        Speaker speaker = findSpeaker(ip);

        return speaker.volume_;
    }

    public void clear() {
        speakers.clear();
    }

    public void setCapture(String ip, int capture){
        Speaker speaker = findSpeaker(ip);
        speaker.capture_ = capture;
    }

    public int getCapture(String ip){
        Speaker speaker = findSpeaker(ip);
        return speaker.capture_;
    }

    private Speaker findSpeaker(String ip) {
        for (Speaker speaker : speakers) {
            if (ip.equals(speaker.ip_)) {
                return speaker;
            }
        }

        return null;
    }
}
