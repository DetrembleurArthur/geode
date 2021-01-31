package com.geode.net;

import java.io.Serializable;
import java.util.ArrayList;

public interface QueryListener
{
    Object listen(ArrayList<Serializable> args);
}
