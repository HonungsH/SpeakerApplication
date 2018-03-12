package com.example.hanneh.speakerapplication;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class ServerCommunication {
    public static final String WARNING = "WARNING";

    private Socket socket_;
    private String host_;
    private int port_;

    private DataInputStream in_stream_;
    private DataOutputStream out_stream_;
    private byte[] buffer_;

    public ServerCommunication(String host, int port) {
        try {
            socket_ = new Socket(host, port);
        } catch (Exception e) {
            Log.e(WARNING, "Could not connect to server");
            e.printStackTrace();

            return;
        }

        host_ = host;
        port_ = port;

        try {
            in_stream_ = new DataInputStream(socket_.getInputStream());
            out_stream_ = new DataOutputStream(socket_.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();

            return;
        }

        buffer_ = new byte[4096];
    }

    public int send(Packet packet) {
        return -1;
    }

    public Packet receive() {
        PartialPacket partial_packet = new PartialPacket();

        while (true) {
            int received;

            try {
                received = in_stream_.read(buffer_, 0, buffer_.length);

                if (received < 0) {
                    throw new Exception();
                }
            } catch (Exception e) {
                Log.e(WARNING, "Lost connection to server");
                e.printStackTrace();

                return new Packet();
            }

            assemblePacket(received, partial_packet);

            if (partial_packet.isFinished()) {
                break;
            }
        }

        return new Packet(partial_packet);
    }

    private void assemblePacket(int received, PartialPacket partial_packet) {
        int processed = 0;

        do {
            processed += processBuffer(Arrays.copyOfRange(buffer_, processed, received), partial_packet);
        } while (processed < received);
    }

    private int processBuffer(byte[] buffer, PartialPacket partial_packet) {
        if (partial_packet.hasHeader()) {
            int insert = buffer.length;

            if (partial_packet.getSize() + buffer.length >= partial_packet.getFullSize()) {
                insert = partial_packet.getFullSize() - partial_packet.getSize();
            }

            partial_packet.addData(buffer, insert);

            return insert;
        } else {
            int header_left = 4 - partial_packet.getSize();
            int adding = buffer.length > header_left ? header_left : buffer.length;

            partial_packet.addData(buffer, adding);

            return adding;
        }
    }
}