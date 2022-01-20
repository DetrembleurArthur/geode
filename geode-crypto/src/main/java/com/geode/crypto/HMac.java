package com.geode.crypto;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class HMac
{
    private Mac mac;

    public static HMac md5(SecretKey key)
    {
        return new HMac("HMAC-MD5", key);
    }

    public HMac(String algo, SecretKey key)
    {
        try
        {
            mac = Mac.getInstance(algo, Global.PROVIDER);
            mac.init(key);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException e)
        {
            e.printStackTrace();
        }
    }

    public HMac feed(byte[] ... arrays)
    {
        for(byte[] array : arrays)
        {
            mac.update(array);
        }
        return this;
    }

    public HMac feedObj(Serializable obj)
    {
        try
        {
            mac.update(Serializer.serialize(obj));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public byte[] hmac()
    {
        return mac.doFinal();
    }
}
