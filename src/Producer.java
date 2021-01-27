import com.geode.annotations.Protocol;
import com.geode.net.Client;
import com.geode.net.ClientProtocolHandler;
import com.geode.net.Geode;
import com.geode.net.Query;

import javax.swing.*;
import java.util.Scanner;

@Protocol("Test")
public class Producer
{
    public static void main(String[] args)
    {
        Geode geode = new Geode("resources/geode.xml");
        Client client = geode.launchClient("Producer");
        ClientProtocolHandler handler = client.getHandlerSafe();

        com.geode.admin.Producer producer = new com.geode.admin.Producer(client);
        producer.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        producer.setVisible(true);
    }
}
