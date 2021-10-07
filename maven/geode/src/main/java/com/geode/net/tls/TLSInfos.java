package com.geode.net.tls;

public class TLSInfos
{
    private String cafile;
    private String certfile;
    private String keyfile;

    public TLSInfos()
    {

    }

    public boolean isTLSEnable()
    {
        return this.cafile != null;
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

    
}
