package com.geode.net.filters;

import com.geode.crypto.Hash;
import com.geode.net.queries.GeodeQuery;

public class ChecksumFilter extends AlertFilter
{
    public static void addChecksum(GeodeQuery query)
    {
        Hash hash = Hash.md5();
        hash.feedObj(query);
        query.getArgs().add(hash.hashStr());
    }

    @Override
    public boolean evaluate(GeodeQuery query)
    {
        String checksum = (String) query.getArgs().get(query.getArgs().size() - 1);
        query.getArgs().remove(checksum);
        Hash hash = Hash.md5();
        hash.feedObj(query);
        String checksum2 = hash.hashStr();
        System.out.println("checksums: " + checksum + " => " + checksum2);
        return checksum.equals(checksum2);
    }

    @Override
    public String getAlert()
    {
        return "CHECKSUM_VIOLATION";
    }
}
