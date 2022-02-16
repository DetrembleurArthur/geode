package com.geode.net.json;

import java.io.Serializable;

import org.json.simple.JSONObject;

public interface JSONSerializable extends Serializable
{
    JSONObject toJson();
    void fromJson(JSONObject jsonObject);
}
