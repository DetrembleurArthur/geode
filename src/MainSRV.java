import com.geode.net.Geode;
import com.geode.annotations.*;
import com.geode.net.UdpHandler;
import com.geode.net.UdpInfos;

@Protocol("Test")
public class MainSRV
{

	public static void main(String[] args)
	{
		Geode geode = new Geode("resources/geode.xml");

		UdpHandler handler = geode.launchUdpHandler("MyUdpServer");
		handler.setDefaultListener(args1 -> System.out.println(">>> " + args1));

	}

	@Control
	public String ping()
	{
		return "pong";
	}
}
