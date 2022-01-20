package com.geode.crypto;

import java.io.IOException;
import java.io.Serializable;
import java.security.*;

public class Sign
{
    private Signature signature;
    private KeyPair keyPair;

    public static Sign sha1WithRsa(KeyPair keyPair)
    {
        return new Sign("SHA1withRSA", keyPair);
    }

    public Sign(String algo, KeyPair keyPair)
    {
        try
        {
            this.keyPair = keyPair;
            signature = Signature.getInstance(algo, Global.PROVIDER);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e)
        {
            e.printStackTrace();
        }
    }

    public Sign signMode()
    {
        try
        {
            signature.initSign(keyPair.getPrivate());
        } catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public Sign verifyMode()
    {
        try
        {
            signature.initVerify(keyPair.getPublic());
        } catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public Sign feed(byte[] ... arrays)
    {
        for(byte[] array : arrays)
        {
            try
            {
                signature.update(array);
            } catch (SignatureException e)
            {
                e.printStackTrace();
            }
        }
        return this;
    }

    public Sign feedObj(Serializable obj)
    {
        try
        {
            signature.update(Serializer.serialize(obj));
        } catch (IOException | SignatureException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public byte[] sign()
    {
        try
        {
            return signature.sign();
        } catch (SignatureException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public boolean verify(byte[] bytesSignature)
    {
        try
        {
            return signature.verify(bytesSignature);
        } catch (SignatureException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
