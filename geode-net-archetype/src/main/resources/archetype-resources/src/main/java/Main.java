import com.geode.net.Geode;


public class Main
{
    public static void main(String[] args)
    {
        if(System.getProperty("log4j.configurationFile") == null)
            System.setProperty("log4j.configurationFile", "geode/log4j.xml");
        Geode geode = Geode.load();
    }
}
