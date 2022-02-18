package com.geode.net.tunnels;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.geode.net.queries.GeodeQuery;
import com.geode.net.queries.GeodeQuery.Category;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class TcpJsonTunnel extends Tunnel<Socket>
{
    private final static Logger logger = LogManager.getLogger(TcpJsonTunnel.class);

    private final JSONParser parser;
    private final BufferedWriter writer;
    private final BufferedReader reader;

    public TcpJsonTunnel(Socket socket) throws IOException
    {
        super(socket);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
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

    public JSONParser getParser()
    {
        return parser;
    }

    public BufferedWriter getWriter()
    {
        return writer;
    }

    public BufferedReader getReader()
    {
        return reader;
    }
}
