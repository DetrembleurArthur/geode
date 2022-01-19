package com.geode.net.net.tls;

public class TLSInfos
{
    private boolean enable = false;
    private String cafile;
    private String certfile;
    private String keyfile;
    private String keystore;
    private String keystorePassword;
    private String keystoreKeyPassword;

    public TLSInfos()
    {

    }

    

    public boolean isTLSEnable() {
        return enable;
    }



    public void setEnable(boolean enable) {
        this.enable = enable;
    }


    

    public String getKeystorePassword() {
        return keystorePassword;
    }



    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }



    public String getKeystoreKeyPassword() {
        return keystoreKeyPassword;
    }



    public void setKeystoreKeyPassword(String keystoreKeyPassword) {
        this.keystoreKeyPassword = keystoreKeyPassword;
    }



    public String getKeystore() {
        return keystore;
    }



    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }


    public String getCafile() {
        return cafile;
    }

    public void setCafile(String cafile) {
        this.cafile = cafile;
    }

    public String getCertfile() {
        return certfile;
    }

    public void setCertfile(String certfile) {
        this.certfile = certfile;
    }

    public String getKeyfile() {
        return keyfile;
    }

    public void setKeyfile(String keyfile) {
        this.keyfile = keyfile;
    }

    @Override
    public String toString()
    {
        return "TLSInfos{" +
                "enable=" + enable +
                ", cafile='" + cafile + '\'' +
                ", certfile='" + certfile + '\'' +
                ", keyfile='" + keyfile + '\'' +
                ", keystore='" + keystore + '\'' +
                '}';
    }
}
