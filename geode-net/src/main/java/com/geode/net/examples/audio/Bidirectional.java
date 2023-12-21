package com.geode.net.examples.audio;

import com.geode.net.communications.UdpDataPipe;
import com.geode.net.communications.audio.MicroManager;
import com.geode.net.connections.UdpSimpleConnection;

import javax.sound.sampled.LineUnavailableException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Bidirectional {
    public static void main(String[] args) throws SocketException, UnknownHostException, LineUnavailableException {
        UdpSimpleConnection connection = UdpSimpleConnection.local(6000);
        connection.setIp(args[0]);
        connection.setPort(6000);
        UdpDataPipe dataPipe = new UdpDataPipe(connection, false);
        MicroManager microManager = new MicroManager(dataPipe);
        microManager.run();
    }
}
