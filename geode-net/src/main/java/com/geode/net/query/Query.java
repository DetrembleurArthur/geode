package com.geode.net.query;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import com.geode.net.communications.*;
import com.geode.net.connections.TcpConnection;
import com.geode.net.connections.UdpSimpleConnection;
import org.json.simple.JSONObject;

public class Query implements Serializable {
    private String type = "UNKNOWN";
    private ArrayList<Serializable> data = new ArrayList<>();


    public static Query Simple(String type) {
        return new Query(type);
    }

    public static Query Success(String type) {
        return new Query(type + ".success");
    }

    public static Query Failed(String type) {
        return new Query(type + ".failed");
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

    public Query success() {
        setType(type + ".success");
        return this;
    }

    public Query failed() {
        setType(type + ".failed");
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

    public Query add(Serializable arg) {
        data.add(arg);
        return this;
    }

    public Query renew() {
        data.clear();
        return this;
    }

    public Query send(Pipe<Serializable> pipe) throws IOException {
        pipe.send(this);
        return this;
    }

    public Query sendAndWait(Pipe<Serializable> pipe) throws Exception {
        pipe.send(this);
        return (Query) pipe.recv();
    }

    public Query sendTCP(String ip, int port, Mode mode) throws IOException {
        TcpConnection connection = TcpConnection.on(ip, port);
        TcpPipe<?, ?, ?> pipe = null;
        switch (mode) {
            case OBJ:
                pipe = new TcpObjectPipe(connection);
                ((TcpObjectPipe)pipe).send(this);
                break;
            case JSON:
                pipe = new TcpJsonPipe(connection);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", type);
                jsonObject.put("data", data);
                ((TcpJsonPipe)pipe).send(jsonObject);
                break;
            case OJSON:
                pipe = new TcpOJsonPipe(connection);
                ((TcpOJsonPipe)pipe).send(this);
                break;
            case STR:
                pipe = new TcpStringPipe(connection);
                ((TcpStringPipe)pipe).send(this.toString());
                break;
        }
        if(pipe != null)
        {
            pipe.close();
            connection.close();
        }
        return null;
    }

    public Query sendUDP(String ip, int port, Mode mode) throws IOException {
        UdpSimpleConnection connection = UdpSimpleConnection.on(ip, port);
        UdpPipe<?> pipe = null;
        switch (mode) {
            case OBJ:
                pipe = new UdpObjectPipe(connection, false);
                ((UdpObjectPipe)pipe).send(this);
                break;
            case JSON:
                pipe = new UdpJsonPipe(connection, false);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", type);
                jsonObject.put("data", data);
                ((UdpJsonPipe)pipe).send(jsonObject);
                break;
            case OJSON:
                pipe = new UdpOJsonPipe(connection, false);
                ((UdpOJsonPipe)pipe).send(this);
                break;
            case STR:
                pipe = new UdpStringPipe(connection, false);
                ((UdpStringPipe)pipe).send(this.toString());
                break;
        }
        if(pipe != null)
        {
            pipe.close();
            connection.close();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Query [type=" + type + ", data=" + data + "]";
    }
}
