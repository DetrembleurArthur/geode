package com.geode.crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;

public class Keys
{
    public static SecretKey aes()
    {
        return generateSecret("AES", 128);
    }

    public static SecretKey des()
    {
        return generateSecret("DES", 64);
    }

    public static KeyPair rsa()
    {
        return generatePair("RSA", 1024);
    }

    public static SecretKey generateSecret(String algo, int size)
    {
        try
        {
            KeyGenerator generator = KeyGenerator.getInstance(algo, Global.PROVIDER);
            generator.init(size, new SecureRandom());
            return generator.generateKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static KeyPair generatePair(String algo, int size)
    {
        try
        {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(algo, Global.PROVIDER);
            generator.initialize(size, new SecureRandom());
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
