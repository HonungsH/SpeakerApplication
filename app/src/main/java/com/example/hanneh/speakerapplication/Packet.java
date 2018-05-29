package com.example.hanneh.speakerapplication;

import java.nio.CharBuffer;
import java.util.*;
import java.nio.ByteBuffer;

public class Packet {
    private ArrayList<Byte> packet_;
    private boolean finalized_;
    private int read_;
    private int sent_;

    public Packet() {
        packet_ = new ArrayList<>();
        finalized_ = false;
        read_ = 0;
        sent_ = 0;
    }

    public Packet(PartialPacket partial_packet) {
        packet_ = partial_packet.getPrivateList();
        finalized_ = true;
        read_ = 0;
        sent_ = 0;

        ArrayList<Byte> final_packet = new ArrayList<>();

        for (int i = 4; i < packet_.size(); i++) {
            final_packet.add(packet_.get(i));
        }

        packet_ = final_packet;
    }

    public void addHeader(byte header) {
        packet_.add(header);
    }

    public void addString(String message) {
        addInt(message.length());
        addBytes(message.getBytes());
    }

    public void addInt(int number) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(number).array();
        addBytes(bytes);
    }

    public void addFloat(float number) {
        String number_string = Float.toString(number);

        packet_.add((byte)number_string.length());
        addBytes(number_string.getBytes());
    }

    public byte getByte() {
        return packet_.get(read_++);
    }

    public int getInt() {
        byte[] bytes = new byte[4];

        bytes[0] = packet_.get(read_++);
        bytes[1] = packet_.get(read_++);
        bytes[2] = packet_.get(read_++);
        bytes[3] = packet_.get(read_++);

        return ByteBuffer.wrap(bytes).getInt();
    }

    public String getString() {
        int length = getInt();
        String message = "";

        for (int i = 0; i < length; i++) {
            message += packet_.get(read_++);
        }

        return message;
    }

    public byte[] getData() {
        byte[] bytes = new byte[packet_.size()];

        for (int i = 0; i < packet_.size(); i++) {
            bytes[i] = packet_.get(i);
        }

        return bytes;
    }

    public int getSize() {
        return packet_.size();
    }

    public boolean isEmpty() {
        return isFinalized() ? packet_.size() <= 4 : packet_.size() == 0;
    }

    public boolean isFinalized() {
        return finalized_;
    }

    public void finalize() {
        if (isFinalized()) {
            return;
        }

        ArrayList<Byte> final_packet = new ArrayList<>();

        byte[] bytes = ByteBuffer.allocate(4).putInt(packet_.size() + 4).array();
        for (int i = 0; i < bytes.length; i++) {
            final_packet.add(bytes[i]);
        }

        final_packet.addAll(packet_);
        packet_ = final_packet;
        finalized_ = true;
    }

    public void addSent(int sent) {
        sent_ += sent;
    }

    public boolean fullySent() {
        return sent_ >= packet_.size();
    }

    public int getSent() {
        return sent_;
    }

    private void addBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            packet_.add(bytes[i]);
        }
    }

    public void addBool(boolean b) {
        packet_.add((byte)(b ? 1 : 0));
    }

    public float getFloat() {
        int length = getInt();

        byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            bytes[i] = packet_.get(read_++);
        }

        CharBuffer buffer = ByteBuffer.wrap(bytes).asCharBuffer();

        return Float.parseFloat(buffer.toString());
    }
}