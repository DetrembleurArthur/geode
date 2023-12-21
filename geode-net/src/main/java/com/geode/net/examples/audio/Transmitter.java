package com.geode.net.examples.audio;

import com.geode.net.communications.UdpDataPipe;
import com.geode.net.communications.audio.MicroTransmitter;
import com.geode.net.connections.UdpSimpleConnection;

public class Transmitter {
    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 6000;
        UdpSimpleConnection udpSimpleConnection = UdpSimpleConnection.client(host, port);
        UdpDataPipe dataPipe = new UdpDataPipe(udpSimpleConnection, false);
        MicroTransmitter transmitter = new MicroTransmitter(dataPipe);
        transmitter.capturing();
        transmitter.start();
    }
}
