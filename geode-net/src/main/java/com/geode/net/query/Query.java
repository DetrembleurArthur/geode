package com.geode.net.query;

import java.io.Serializable;

public class Query implements Serializable{
    private String type = "UNKNOWN";
    private Serializable data;

    public Query() {
    }

    public Query(String type) {
        this.type = type;
    }

    public Query(String type, Serializable data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Serializable getData() {
        return data;
    }
    
    public void setData(Serializable data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Query [type=" + type + ", data=" + data + "]";
    }
}
