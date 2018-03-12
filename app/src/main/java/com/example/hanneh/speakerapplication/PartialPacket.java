package com.example.hanneh.speakerapplication;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class PartialPacket {
    private ArrayList<Byte> packet_;
    private int total_size_;

    public PartialPacket() {
        packet_ = new ArrayList<>();
        total_size_ = 0;
    }

    public boolean hasHeader() {
        return total_size_ != 0;
    }

    public int getSize() {
        return packet_.size();
    }

    public int getFullSize() {
        return total_size_;
    }

    public void addData(byte[] buffer, int size) {
        for (int i = 0; i < size; i++) {
            packet_.add(buffer[i]);
        }

        if (total_size_ == 0 && packet_.size() >= 4) {
            setFullSize();
        }
    }

    public boolean isFinished() {
        return total_size_ != 0 ? packet_.size() == total_size_ : false;
    }

    public ArrayList<Byte> getPrivateList() {
        return packet_;
    }

    private void setFullSize() {
        byte[] bytes = { packet_.get(0), packet_.get(1), packet_.get(2), packet_.get(3) };

        total_size_ = ByteBuffer.wrap(bytes).getInt();
    }
}