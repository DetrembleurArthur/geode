package com.geode.net.tls;

import com.geode.logging.Logger;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Scanner;

public class TLSUtils
{
    private static final Logger logger = new Logger(TLSUtils.class);

    public static X509Certificate readCert(final String fname) throws CertificateException, FileNotFoundException
    {
        logger.info("read certificate : " + fname);
        return (X509Certificate) CertificateFactory
                .getInstance("X.509")
                .generateCertificate(new FileInputStream(fname));
    }

    public static KeyStore loadKeystore(final String keystoreFile, final String password) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException
    {
        logger.info("load keystore : " + keystoreFile);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream(keystoreFile), password.toCharArray());
        return ks;
    }


    public static PrivateKey getPrivateKey(String filename) throws Exception
    {
        logger.info("load private key : " + filename);
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

    public static SSLContext getSSLContext(final String keystoreFile) throws Exception
    {
        String password;
        Scanner scanner = new Scanner(System.in);
        System.err.print("keystore password: ");
        password = new String(System.console().readPassword());
        KeyStore keyStore = loadKeystore(keystoreFile, password);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

        tmf.init(keyStore);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");

        System.err.print("private key password: ");
        password = new String(System.console().readPassword());
        scanner.close();
        kmf.init(keyStore, password.toCharArray());

        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return context;
    }

    public static SSLSocketFactory getSocketFactory(final String keystore) throws Exception
    {
        return getSSLContext(keystore).getSocketFactory();
    }

    public static SSLServerSocketFactory getServerSocketFactory(final String keystore) throws Exception
    {
        return getSSLContext(keystore).getServerSocketFactory();
    }

    public static SSLContext getSSLContext(final String caCrtFile, final String certFile, final String keyFile) throws Exception
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
        logger.info("ssl context initialized");

        return context;
    }

    public static SSLSocketFactory getSocketFactory(final String caCrtFile, final String certFile, final String keyFile) throws Exception
    {
        return getSSLContext(caCrtFile, certFile, keyFile).getSocketFactory();
    }

    public static SSLServerSocketFactory getServerSocketFactory(final String caCrtFile, final String certFile, final String keyFile) throws Exception
    {
        return getSSLContext(caCrtFile, certFile, keyFile).getServerSocketFactory();
    }

    public static SSLSocketFactory getSocketFactory(TLSInfos infos) throws Exception
    {
        if (infos.getKeystore() != null)
            return getSocketFactory(infos.getKeystore());
        else
            return getSocketFactory(infos.getCafile(), infos.getCertfile(), infos.getKeyfile());
    }

    public static SSLServerSocketFactory getServerSocketFactory(TLSInfos infos) throws Exception
    {
        if (infos.getKeystore() != null)
            return getServerSocketFactory(infos.getKeystore());
        else
            return getServerSocketFactory(infos.getCafile(), infos.getCertfile(), infos.getKeyfile());
    }
}
