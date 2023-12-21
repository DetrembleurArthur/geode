package com.geode.net.examples.audio;

import com.geode.net.communications.UdpDataPipe;
import com.geode.net.communications.audio.MicroTransmitter;
import com.geode.net.connections.UdpSimpleConnection;

public class Transmitter {
    public static void main(String[] args) throws Exception {
        UdpSimpleConnection udpSimpleConnection = UdpSimpleConnection.client("127.0.0.1", 6000);
        UdpDataPipe dataPipe = new UdpDataPipe(udpSimpleConnection, false);
        MicroTransmitter transmitter = new MicroTransmitter(dataPipe);
        transmitter.capturing();
        transmitter.start();
        Thread.sleep(3000);
        transmitter.pause();
        Thread.sleep(3000);
        transmitter.unpause();
    }
}
