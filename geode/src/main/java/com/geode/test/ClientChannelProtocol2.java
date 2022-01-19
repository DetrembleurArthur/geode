package com.geode.test;

import com.geode.annotations.Control;
import com.geode.annotations.OnEvent;
import com.geode.annotations.Protocol;
import com.geode.annotations.Register;
import com.geode.net.channels.Channel;

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
