package com.geode.net.communications;

import com.geode.net.connections.Connection;
import com.geode.net.connections.TcpConnection;
import com.geode.net.connections.UdpConnection;
import com.geode.net.connections.UdpSimpleConnection;

import java.io.IOException;

public class PipeFactory {
    public static Pipe create(Mode mode, boolean tcp, Connection connection) throws IOException {
        switch (mode)
        {
            case JSON: return tcp ? new TcpJsonPipe((TcpConnection) connection) : new UdpJsonPipe((UdpConnection<?>) connection, false);
            case OBJ: return tcp ? new TcpObjectPipe((TcpConnection) connection) : new UdpObjectPipe((UdpConnection<?>) connection, false);
            case STR: return tcp ? new TcpStringPipe((TcpConnection) connection) : new UdpStringPipe((UdpConnection<?>) connection, false);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        UdpSimpleConnection connection = UdpSimpleConnection.internal(8888);
        UdpStringPipe pipe = (UdpStringPipe) PipeFactory.create(Mode.STR, false, connection);
        pipe.send("Hello world !");
        System.out.println(pipe.recv());
    }
}
