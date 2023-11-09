package com.geode.crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

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

    public static PrivateKey readPrivateKey(String filename) throws Exception
    {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static X509Certificate readCert(final String fname) throws CertificateException, FileNotFoundException
    {
        return (X509Certificate) CertificateFactory
                .getInstance("X.509")
                .generateCertificate(new FileInputStream(fname));
    }
}
