package com.geode.net.conf;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Loader("json")
public class JsonConfigurationLoader extends ConfigurationLoader
{
    static
    {
        ConfigurationLoader.register(JsonConfigurationLoader.class);
    }

    private final JSONParser parser;

    public JsonConfigurationLoader(File confFile)
    {
        super(confFile);
        parser = new JSONParser();
    }

    @Override
    public Map<String, Object> parse()
    {
        try(FileReader reader = new FileReader(getConfFile()))
        {
            return (Map<String, Object>) parser.parse(reader);
        } catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
