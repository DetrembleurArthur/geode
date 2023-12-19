package com.geode.net.examples;

import com.geode.net.communications.Pipe;
import com.geode.net.communications.TcpStringPipe;
import com.geode.net.connections.TcpConnection;
import com.geode.net.connections.TcpStickyConnection;

import java.io.IOException;
import java.net.http.HttpRequest;

public class SimpleHttp {
    public static void main(String[] args) throws Exception {
        TcpStickyConnection stickyConnection = TcpStickyConnection.local(80, 5);
        while(true)
        {
            TcpConnection connection = stickyConnection.accept();
            Pipe pipe = new TcpStringPipe(connection);
            System.out.println(pipe.recv());
            pipe.send("<h1>Hello world !</h1>");
            pipe.close();
        }
    }
}
