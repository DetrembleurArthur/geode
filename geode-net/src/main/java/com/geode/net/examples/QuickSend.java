package com.geode.net.examples;

import com.geode.net.communications.Mode;
import com.geode.net.query.Query;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class QuickSend {
    public static void main(String[] args) throws IOException {
        Query.Simple("hello").add(Date.from(Instant.now()).toString()).sendUDP("127.0.0.1", 5000, Mode.OJSON);
    }
}
