import com.geode.annotations.Protocol;
import com.geode.net.Client;
import com.geode.net.ClientProtocolHandler;
import com.geode.net.Geode;

import javax.swing.*;

@Protocol("Test")
public class Producer
{
    public static void main(String[] args)
    {
        Geode geode = new Geode("resources/geode.xml");
        Client tcpClient = geode.launchClient("Producer");
        ClientProtocolHandler handler = tcpClient.getHandlerSafe();

        com.geode.admin.Producer producer = new com.geode.admin.Producer(tcpClient);
        producer.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        producer.setVisible(true);
    }
}
