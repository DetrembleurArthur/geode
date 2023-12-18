package com.geode.net.examples;

import com.geode.net.communications.TcpDataPipe;
import com.geode.net.connections.TcpConnection;
import com.geode.net.connections.TcpStickyConnection;

import java.util.Arrays;

public class TcpDataTransfertServer
{

    public static void main(String[] args) throws Exception
    {
        TcpStickyConnection server = TcpStickyConnection.internal(6000, 5);
        TcpConnection clientHandler = server.accept();
        TcpDataPipe clientHandlerPipe = new TcpDataPipe(clientHandler);
        
        byte[] data = (byte[]) clientHandlerPipe.recv();
        System.out.println(Arrays.toString(data));

        clientHandlerPipe.close();
        server.close();
    }
}
