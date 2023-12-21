package com.geode.net.examples.audio;

import com.geode.net.communications.UdpDataPipe;
import com.geode.net.communications.audio.MicroReceiver;
import com.geode.net.connections.UdpSimpleConnection;

public class Receiver {
    public static void main(String[] args) throws Exception {
        UdpSimpleConnection udpSimpleConnection = UdpSimpleConnection.on("127.0.0.1", 6000);
        UdpDataPipe dataPipe = new UdpDataPipe(udpSimpleConnection, true);
        MicroReceiver receiver = new MicroReceiver(dataPipe);
        receiver.start();
    }
}
