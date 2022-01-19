package com.geode.net.net.test;

import com.geode.net.net.annotations.Control;
import com.geode.net.net.annotations.OnEvent;
import com.geode.net.net.annotations.Protocol;
import com.geode.net.net.annotations.Register;
import com.geode.net.net.channels.Channel;

@Protocol("p2")
public class ClientChannelProtocol2
{
    @Register("square")
    public Channel<Integer> square;

    public Channel<String> message;

    @OnEvent
    public void init()
    {
        System.out.println(message.get());
    }

    @Control
    public void square_success(Integer value)
    {
        square.notifyValue(value);
    }
}
