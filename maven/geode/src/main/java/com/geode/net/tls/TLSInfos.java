package com.geode.net.tls;

public class TLSInfos
{
    private boolean enable = false;
    private String cafile;
    private String certfile;
    private String keyfile;
    private String keystore;

    public TLSInfos()
    {

    }

    

    public boolean isTLSEnable() {
        return enable;
    }



    public void setEnable(boolean enable) {
        this.enable = enable;
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
