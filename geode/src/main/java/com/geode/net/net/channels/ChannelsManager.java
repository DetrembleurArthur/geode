package com.geode.net.net.channels;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.geode.net.net.annotations.Register;
import com.geode.net.net.logging.Logger;

public class ChannelsManager
{
    private static final Logger logger = new Logger(ChannelsManager.class);

    private final HashMap<String, Channel<?>> channels = new HashMap<>();

    public synchronized void register(Object protocol, ChannelsManagerInfos infos) throws Exception
    {
        logger.info("init channel registration");
        Field[] fields = protocol.getClass().getDeclaredFields();
        for(Field field : fields)
        {
            if((!infos.isStrict() && field.getType().equals(Channel.class)) || field.isAnnotationPresent(Register.class))
            {
                Register register = field.getAnnotation(Register.class);
                String id = (register == null || register.value().isBlank()) ? field.getName() : register.value();
                if(!channels.containsKey(id))
                {
                    if(field.getType().equals(Channel.class))
                    {
                        channels.put(id, (Channel<?>) field.getType().getConstructor().newInstance());
                        logger.info("'" + field.getName() + "' channel registred");
                    }
                    else
                    {
                        throw new Exception("can not create channel from a non Channel field type");
                    }
                }
                if(field.getType().equals(Channel.class))
                {
                    field.set(protocol, channels.get(id));
                }
                else
                {
                    field.set(protocol, channels.get(id).get());
                }
                logger.info("'" + field.getName() + "' channel injected");
            }
        }
        logger.info("end channel registration");
    }

    public synchronized <T> Channel<T> createChannel(String id)
    {
        Channel<T> channel = new Channel<>();
        channels.put(id, channel);
        return channel;
    }

    public synchronized Channel<?> channel(String id)
    {
        return channels.get(id);
    }
}
