package com.geode.net.tls;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Scanner;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class TLSUtils
{
    public static X509Certificate readCert(final String fname) throws CertificateException, FileNotFoundException
    {
        return (X509Certificate) CertificateFactory
                .getInstance("X.509")
                .generateCertificate(new FileInputStream(fname));
    }

    public static KeyStore loadKeystore(final String keystoreFile, final String password) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException
    {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream(keystoreFile), password.toCharArray());
        return ks;
    }

    
    public static PrivateKey getPrivateKey(String filename) throws Exception {

        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf =
                KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static SSLContext getSSLContext(final String keystoreFile) throws Exception
    {
        String password;
        Scanner scanner = new Scanner(System.in);
        System.err.print("keystore password: ");
        password = scanner.nextLine();
        KeyStore keyStore = loadKeystore(keystoreFile, password);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

        tmf.init(keyStore);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");

        System.err.print("private key password: ");
        password = scanner.nextLine();
        scanner.close();
        kmf.init(keyStore, password.toCharArray());

        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return context;
    }

    public static SSLSocketFactory getSocketFactory (final String keystore) throws Exception
    {
        return getSSLContext(keystore).getSocketFactory();
    }

    public static SSLServerSocketFactory getServerSocketFactory (final String keystore) throws Exception
    {
        return getSSLContext(keystore).getServerSocketFactory();
    }

    public static SSLContext getSSLContext (final String caCrtFile, final String certFile, final String keyFile) throws Exception
    {
        KeyStore caKs = KeyStore.getInstance("JKS");
        caKs.load(null, null);

        X509Certificate caCert = readCert(caCrtFile);
        
        caKs.setCertificateEntry("CA", caCert);

        X509Certificate cliCert = readCert(certFile);
        caKs.setCertificateEntry("CLIcert", cliCert);

        PrivateKey key = getPrivateKey(keyFile);
        caKs.setKeyEntry("CLIkey", key, "".toCharArray(), new Certificate[]{cliCert});
        
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");


        tmf.init(caKs);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(caKs, "".toCharArray());

        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return context;
    }

    public static SSLSocketFactory getSocketFactory (final String caCrtFile, final String certFile, final String keyFile) throws Exception
    {
        return getSSLContext(caCrtFile, certFile, keyFile).getSocketFactory();
    }

    public static SSLServerSocketFactory getServerSocketFactory (final String caCrtFile, final String certFile, final String keyFile) throws Exception
    {
        return getSSLContext(caCrtFile, certFile, keyFile).getServerSocketFactory();
    }

    public static SSLSocketFactory getSocketFactory(TLSInfos infos) throws Exception
    {
        if(infos.getKeystore() != null)
            return getSocketFactory(infos.getKeystore());
        else
            return getSocketFactory(infos.getCafile(), infos.getCertfile(), infos.getKeyfile());
    }

    public static SSLServerSocketFactory getServerSocketFactory(TLSInfos infos) throws Exception
    {
        if(infos.getKeystore() != null)
            return getServerSocketFactory(infos.getKeystore());
        else
            return getServerSocketFactory(infos.getCafile(), infos.getCertfile(), infos.getKeyfile());
    }
}
