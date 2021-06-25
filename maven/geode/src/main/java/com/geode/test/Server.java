package com.geode.test;

import com.geode.net.Geode;
import com.geode.net.ServerInfos;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class Server
{
    public static void main(String[] args) throws Exception
    {
        Geode geode = new Geode();
        geode.init("src/main/resources/conf.yml");
        geode.launchServer("pingServer");
    }
}
