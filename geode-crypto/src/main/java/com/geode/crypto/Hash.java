package com.geode.crypto;

import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Hash
{
    private MessageDigest digest;

    public static Hash md5()
    {
        return new Hash("MD5");
    }

    public static Hash sha1()
    {
        return new Hash("SHA-1");
    }

    public static Hash sha224()
    {
        return new Hash("SHA-224");
    }

    public static Hash sha256()
    {
        return new Hash("SHA-256");
    }

    public static Hash sha384()
    {
        return new Hash("SHA-384");
    }

    public static Hash sha512()
    {
        return new Hash("SHA-512");
    }

    public Hash(String algo)
    {
        try
        {
            digest = MessageDigest.getInstance(algo, Global.PROVIDER);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e)
        {
            e.printStackTrace();
        }
    }

    public Hash feed(byte[] ... arrays)
    {
        for(byte[] array : arrays)
        {
            digest.update(array);
        }
        return this;
    }

    public Hash feedObj(Serializable obj)
    {
        try
        {
            digest.update(Serializer.serialize(obj));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public byte[] hash()
    {
        return digest.digest();
    }

    public String hashStr()
    {
        return Serializer.bytesToString(hash());
    }
}
