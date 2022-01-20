package com.geode.crypto;

import javax.crypto.*;
import java.io.IOException;
import java.io.Serializable;
import java.security.*;

public class Encrypter
{
    private Cipher cipher;
    private Serializable key;

    public static Encrypter des(SecretKey key)
    {
        return new Encrypter("DES", "ECB", "PKCS5Padding", key);
    }

    public static Encrypter aes(SecretKey key)
    {
        return new Encrypter("AES", "ECB", "PKCS5Padding", key);
    }

    public static Encrypter rsa(KeyPair key)
    {
        return new Encrypter("RSA", "ECB", "PKCS1Padding", key);
    }

    public Encrypter(String cryptAlgo, String blocAlgo, String paddAlgo, Serializable key)
    {
        try
        {
            cipher = Cipher.getInstance(cryptAlgo + "/" + blocAlgo + "/" + paddAlgo, Global.PROVIDER);
            this.key = key;
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
    }

    public Encrypter mode(int mode, Key key)
    {
        try
        {
            cipher.init(mode, key);
        } catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public Encrypter encryptMode()
    {
        return mode(Cipher.ENCRYPT_MODE, key instanceof SecretKey ? (SecretKey)key : ((KeyPair)key).getPublic());
    }

    public Encrypter decryptMode()
    {
        return mode(Cipher.DECRYPT_MODE, key instanceof SecretKey ? (SecretKey)key : ((KeyPair)key).getPrivate());
    }

    public Encrypter feed(byte[] ... arrays)
    {
        for(byte[] array : arrays)
        {
            cipher.update(array);
        }
        return this;
    }

    public Encrypter feedObj(Serializable obj)
    {
        try
        {
            cipher.update(Serializer.serialize(obj));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public byte[] apply()
    {
        try
        {
            return cipher.doFinal();
        } catch (IllegalBlockSizeException | BadPaddingException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String cryptStr()
    {
        return Serializer.bytesToString(apply());
    }
}
