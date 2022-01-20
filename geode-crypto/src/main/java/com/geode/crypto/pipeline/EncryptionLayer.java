package com.geode.crypto.pipeline;

import com.geode.crypto.Encrypter;

import java.io.Serializable;

public class EncryptionLayer implements Layer
{
    private Encrypter encrypter;

    public EncryptionLayer(Encrypter encrypter)
    {
        this.encrypter = encrypter;
    }

    @Override
    public Serializable in(Serializable o)
    {
        return encrypter.encryptMode().feedObj(o).apply();
    }

    @Override
    public Serializable out(Serializable o)
    {
        return encrypter.decryptMode().feedObj(o).apply();
    }
}
