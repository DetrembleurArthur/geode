package com.geode.crypto;

import org.bouncycastle.jcajce.provider.drbg.DRBG;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class Store
{
    private KeyStore keyStore;
    private String filename;
    private String password;

    public static Store jks()
    {
        return new Store("BKS");
    }

    public static Store jce()
    {
        return new Store("JCE");
    }

    public static Store pkcs12()
    {
        return new Store("PKCS12");
    }

    public static Store jceks()
    {
        return new Store("JCEKS");
    }

    public static Store def()
    {
        return new Store(KeyStore.getDefaultType());
    }

    public Store(String type)
    {
        try
        {
            keyStore = KeyStore.getInstance(type, Global.PROVIDER);
        } catch (KeyStoreException | NoSuchProviderException e)
        {
            e.printStackTrace();
        }
    }

    public Store(String filename, String password)
    {
        try
        {
            keyStore = KeyStore.getInstance(new File(filename), password.toCharArray());
            this.filename = filename;
            this.password = password;
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e)
        {
            e.printStackTrace();
        }
    }

    public Store create(String filename, String password)
    {
        try
        {
            keyStore.load(new FileInputStream(filename), password.toCharArray());
            this.filename = filename;
            this.password = password;
        } catch (IOException | NoSuchAlgorithmException | CertificateException e)
        {
            try
            {
                keyStore.load(null, password.toCharArray());
                this.password = password;
            } catch (IOException | NoSuchAlgorithmException | CertificateException ex)
            {
                ex.printStackTrace();
            }
        }
        return this;
    }

    public Store setKey(Key key, String alias, String password)
    {
        try
        {
            keyStore.setKeyEntry(alias, key, password.toCharArray(), null);
        } catch (KeyStoreException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public Store setCertificate(Certificate certificate, String alias)
    {
        try
        {
            keyStore.setCertificateEntry(alias, certificate);
        } catch (KeyStoreException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public Store save(String filename, String password)
    {
        this.filename = filename;
        this.password = password;
        try
        {
            keyStore.store(new FileOutputStream(filename), password.toCharArray());
        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return this;
    }
}
