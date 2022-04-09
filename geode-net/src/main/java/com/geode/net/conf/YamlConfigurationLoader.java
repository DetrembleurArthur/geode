package com.geode.net.conf;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

@Loader("yaml")
public class YamlConfigurationLoader extends ConfigurationLoader
{
    static
    {
        ConfigurationLoader.register(YamlConfigurationLoader.class);
    }

    private final Yaml yaml;

    public YamlConfigurationLoader(File confFile)
    {
        super(confFile);
        yaml = new Yaml();
    }

    @Override
    public Map<String, Object> parse() throws FileNotFoundException
    {
        return yaml.load(new FileInputStream(getConfFile()));
    }
}
