package com.geode.net.examples;

import com.geode.net.communications.TcpDataPipe;
import com.geode.net.connections.TcpConnection;
import com.geode.net.connections.TcpStickyConnection;

import java.util.Arrays;

public class TcpDataTransfertClient {

    public static void main(String[] args) throws Exception {
        TcpConnection client = TcpConnection.internal(6000);

        TcpDataPipe clientPipe = new TcpDataPipe(client);

        byte[] data = new byte[]{1, 2, 3};

        clientPipe.send(data);

        clientPipe.close();
    }
}
