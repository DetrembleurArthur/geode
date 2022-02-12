package com.geode.net.tunnels;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.Socket;

import com.geode.net.queries.GeodeQuery;
import com.geode.net.queries.GeodeQuery.Category;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Tcp tunnel.
 */
public class TcpStringTunnel extends Tunnel<Socket>
{
    private static final Logger logger = LogManager.getLogger(TcpStringTunnel.class);
    private static final String FIELDS_SEPARATOR = "::";
    private final BufferedWriter writer;
    private final BufferedReader reader;

    /**
     * Instantiates a new Tcp tunnel.
     *
     * @param socket the socket
     * @throws IOException the io exception
     */
    public TcpStringTunnel(Socket socket) throws IOException
    {
        super(socket);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
        logger.info("tunnel initialized on " + socket);
    }

    @Override
    public void send(Serializable serializable) throws IOException
    {
        logger.info("send " + serializable);
        if(serializable instanceof GeodeQuery)
        {
            GeodeQuery query = (GeodeQuery)serializable;
            StringBuilder builder = new StringBuilder(query.getType());
            builder.append(FIELDS_SEPARATOR)
            .append(query.getCategory().name());
            for(Serializable ser : query.getArgs())
            {
                builder.append(FIELDS_SEPARATOR);
                if(ser instanceof Integer)
                    builder.append("int.").append(String.valueOf(ser));
                else if(ser instanceof Float)
                    builder.append("float.").append(String.valueOf(ser));
                else if(ser instanceof Boolean)
                    builder.append("boo.").append(String.valueOf(ser));
                else if(ser instanceof String)
                    builder.append("str.").append(ser);
            }
            logger.info("string query: " + builder);
            writer.append(builder).append(".\n");
        }
        else
            writer.append((CharSequence) serializable).append(".\n");
        writer.flush();
    }

    @Override
    public <T extends Serializable> T recv() throws IOException
    {
        String buffer = "", temp;
        while((temp = reader.readLine()) != null)
        {
            logger.info("temp recv: " + temp);
            buffer += temp;
            if(temp.endsWith("."))
                break;
        }
        logger.info("recv: " + buffer);
        return (T) buffer;
    }

    public GeodeQuery recvQuery() throws IOException
    {
        GeodeQuery query = new GeodeQuery();
        String squery = recv();
        String[] tokens = squery.split(FIELDS_SEPARATOR);
        if(tokens.length < 2)
        {
            send(GeodeQuery.failed("response").pack("query must have at least 2 fields"));
            return null;
        }
        query.setType(tokens[0]);
        query.setCategory(Category.valueOf(tokens[1].toUpperCase()));
        for(int i = 2; i < tokens.length; i++)
        {
            if(i == tokens.length - 1)
                tokens[i] = tokens[i].substring(0, tokens[i].length() - 1);
            if(tokens[i].startsWith("int."))
                query.getArgs().add(Integer.valueOf(tokens[i].substring(4)));
            else if(tokens[i].startsWith("flo."))
                query.getArgs().add(Float.valueOf(tokens[i].substring(4)));
            else if(tokens[i].startsWith("boo."))
                query.getArgs().add(Boolean.valueOf(tokens[i].substring(4)));
            else if(tokens[i].startsWith("str."))
                query.getArgs().add(tokens[i].substring(4));
        }
        return query;
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
