package com.geode.crypto;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


public class Tls
{

    public static SSLContext getSSLContextKeystore(final Store keystore) throws Exception
    {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

        tmf.init(keystore.getKeyStore());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");

        kmf.init(keystore.getKeyStore(), keystore.getPassword().toCharArray());

        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return context;
    }

    public static SSLSocketFactory getSocketFactoryKeystore(final Store keystore) throws Exception
    {
        return getSSLContextKeystore(keystore).getSocketFactory();
    }

    public static SSLServerSocketFactory getServerSocketFactoryKeystore(final Store keystore) throws Exception
    {
        return getSSLContextKeystore(keystore).getServerSocketFactory();
    }

    public static SSLContext getSSLContext(final String caCrtFile, final String certFile, final String keyFile) throws Exception
    {
        KeyStore caKs = KeyStore.getInstance("JKS");
        caKs.load(null, null);

        X509Certificate caCert = Keys.readCert(caCrtFile);

        caKs.setCertificateEntry("CA", caCert);

        X509Certificate cliCert = Keys.readCert(certFile);
        caKs.setCertificateEntry("CLIcert", cliCert);

        PrivateKey key = Keys.readPrivateKey(keyFile);
        caKs.setKeyEntry("CLIkey", key, "".toCharArray(), new Certificate[]{cliCert});

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");


        tmf.init(caKs);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(caKs, "".toCharArray());

        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

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
}
