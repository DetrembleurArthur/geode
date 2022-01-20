package com.geode.crypto.pipeline;

import java.io.Serializable;

public interface Layer
{
    Serializable in(Serializable o);
    Serializable out(Serializable o);
}
