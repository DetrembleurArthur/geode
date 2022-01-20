package com.geode.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Global
{
    public static final String PROVIDER = "BC";

    static
    {
        if(Security.getProvider(PROVIDER) == null)
            Security.addProvider(new BouncyCastleProvider());
    }
}
