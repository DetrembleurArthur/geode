package com.geode.net.examples;

import com.geode.net.communications.UdpDataPipe;
import com.geode.net.communications.UdpObjectPipe;
import com.geode.net.connections.UdpSimpleConnection;

public class UdpDataTransfertServer {
    public static void main(String[] args) throws Exception {
        UdpSimpleConnection server = UdpSimpleConnection.internal(6000);

        UdpDataPipe serverPipe = new UdpDataPipe(server, true);

        byte[] data = (byte[]) serverPipe.recv();
        for (byte d : data) {
            System.out.println(Byte.toUnsignedInt(d));

        }

    }
}
