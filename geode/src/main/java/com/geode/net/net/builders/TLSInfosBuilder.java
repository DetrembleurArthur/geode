package com.geode.net.net.builders;

import com.geode.net.net.annotations.Attribute;
import com.geode.net.net.tls.TLSInfos;

public class TLSInfosBuilder extends Builder<TLSInfos>
{
    static
    {
        BuildersMap.register(TLSInfos.class, TLSInfosBuilder.class);
    }

    public TLSInfosBuilder()
    {
        reset();
    }

    public static TLSInfosBuilder create()
    {
        return new TLSInfosBuilder();
    }

    @Override
    public TLSInfosBuilder reset()
    {
        object = new TLSInfos();
        return this;
    }

    @Attribute("keystore")
    public TLSInfosBuilder keystore(String keystore)
    {
        object.setKeystore(keystore);
        return this;
    }

    @Attribute("ca")
    public TLSInfosBuilder cafile(String cafile)
    {
        object.setCafile(cafile);
        return this;
    }

    @Attribute("cert")
    public TLSInfosBuilder certfile(String certfile)
    {
        object.setCertfile(certfile);
        return this;
    }

    @Attribute("enable")
    public TLSInfosBuilder enable(Boolean enable)
    {
        object.setEnable(enable);
        return this;
    }

    @Attribute("key")
    public TLSInfosBuilder keyfile(String keyfile)
    {
        object.setKeyfile(keyfile);
        return this;
    }

    @Attribute("keystore-password")
    public TLSInfosBuilder keystorePassword(String keystorePassword)
    {
        object.setKeystorePassword(keystorePassword);
        return this;
    }

    @Attribute("key-password")
    public TLSInfosBuilder keystoreKeyPassword(String keystoreKeyPassword)
    {
        object.setKeystoreKeyPassword(keystoreKeyPassword);
        return this;
    }
}
