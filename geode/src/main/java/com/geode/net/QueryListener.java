package com.geode.net;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The interface Query listener.
 */
public interface QueryListener
{
    /**
     * Listen object.
     *
     * @param args the args
     * @return the object
     */
    Object listen(ArrayList<Serializable> args);
}
