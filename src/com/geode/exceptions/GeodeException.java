package com.geode.exceptions;

import com.geode.net.GeodeIdentifiable;

public class GeodeException extends Exception
{
    private final GeodeIdentifiable geodeIdentifiable;

    public GeodeException(GeodeIdentifiable geodeIdentifiable, String message)
    {
        super("[" + geodeIdentifiable.getGeodeId() + "] => " + message);
        this.geodeIdentifiable = geodeIdentifiable;
    }

    public GeodeIdentifiable getGeodeIdentifiable()
    {
        return geodeIdentifiable;
    }
}
