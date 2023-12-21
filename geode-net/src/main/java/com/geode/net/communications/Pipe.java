package com.geode.net.communications;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;

public interface Pipe extends Closeable
{
    void send(Serializable data) throws IOException;
    Serializable recv() throws Exception;
    boolean available();
}
