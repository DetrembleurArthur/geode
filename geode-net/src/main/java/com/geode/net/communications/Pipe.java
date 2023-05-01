package com.geode.net.communications;

import org.json.simple.parser.ParseException;

import java.io.Closeable;
import java.io.IOException;

public interface Pipe<T> extends Closeable
{
    void send(T data) throws IOException;
    T recv() throws IOException, ParseException, ClassNotFoundException;
}
