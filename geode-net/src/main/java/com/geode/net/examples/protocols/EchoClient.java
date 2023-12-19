package com.geode.net.examples.protocols;

import com.geode.net.protocols.*;
import com.geode.net.query.Query;

@Protocol("echo")
public class EchoClient {

        @Target("echo")
        public Query echo(Integer n)
        {
            n--;
            if(n > 0)
                return Query.Simple("echo").add(n);
            return null;
        }
    }