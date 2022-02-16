package com.geode.net.tunnels;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.Socket;
import java.util.Arrays;

import com.geode.net.queries.GeodeQuery;
import com.geode.net.queries.GeodeQuery.Category;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class TcpJsonTunnel extends TcpStringTunnel
{
    private final static Logger logger = LogManager.getLogger(TcpJsonTunnel.class);

    private JSONParser parser;

    public TcpJsonTunnel(Socket socket) throws IOException
    {
        super(socket);
        parser = new JSONParser();
    }

    @Override
    public <T extends Serializable> T recv() throws IOException
    {
        String buffer = getReader().readLine();
        logger.info("recv: " + buffer);
        try {
            JSONObject json = (JSONObject)parser.parse(buffer);
            return (T)json;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public GeodeQuery recvQuery() throws IOException
    {
        JSONObject jsonObject = recv();
        GeodeQuery query = new GeodeQuery();
        query.fromJson(jsonObject);
        return query;
    }

    @Override
    public void send(Serializable serializable) throws IOException
    {
        GeodeQuery query = (GeodeQuery)serializable;
        JSONObject jsonObject = query.toJson();
        logger.info("send: " + jsonObject);
        jsonObject.writeJSONString(getWriter());
        getWriter().newLine();
        getWriter().flush();
    }
}
