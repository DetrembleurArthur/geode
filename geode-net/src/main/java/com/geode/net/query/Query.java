package com.geode.net.query;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import com.geode.net.communications.Pipe;

public class Query implements Serializable{
    private String type = "UNKNOWN";
    private ArrayList<Serializable> data = new ArrayList<>();


    public static Query Simple(String type)
    {
        return new Query(type);
    }

    public static Query Success(String type)
    {
        return new Query(type+".success");
    }

    public static Query Failed(String type)
    {
        return new Query(type+".failed");
    }

    public Query() {
    }

    public Query(String type) {
        this.type = type;
    }

    public Query(String type, ArrayList<Serializable> data) {
        this.type = type;
        this.data = data;
    }

    public Query success()
    {
        setType(type+".success");
        return this;
    }

    public Query failed()
    {
        setType(type+".failed");
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Serializable> getData() {
        return data;
    }
    
    public void setData(ArrayList<Serializable> data) {
        this.data = data;
    }

    public Query add(Serializable arg)
    {
        data.add(arg);
        return this;
    }

    public Query renew()
    {
        data.clear();
        return this;
    }

    public Query send(Pipe<Serializable> pipe) throws IOException
    {
        pipe.send(this);
        return this;
    }

    public Query sendAndWait(Pipe<Serializable> pipe) throws Exception
    {
        pipe.send(this);
        return (Query) pipe.recv();
    }

    @Override
    public String toString() {
        return "Query [type=" + type + ", data=" + data + "]";
    }
}
