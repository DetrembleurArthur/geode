package com.geode.net.examples.audio;

import com.geode.net.communications.UdpDataPipe;
import com.geode.net.communications.audio.MicroReceiver;
import com.geode.net.connections.UdpSimpleConnection;

public class Receiver {
    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 6000;
        UdpSimpleConnection udpSimpleConnection = UdpSimpleConnection.on(host, port);
        UdpDataPipe dataPipe = new UdpDataPipe(udpSimpleConnection, false);
        MicroReceiver receiver = new MicroReceiver(dataPipe);
        receiver.start();
    }
}
