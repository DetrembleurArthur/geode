package com.geode.net.communications;

import com.geode.net.connections.UdpConnection;

public class UdpDataPipe extends UdpPipe {
    public UdpDataPipe(UdpConnection<?> connection, boolean resendMode) {
        super(connection, resendMode);
    }
}
