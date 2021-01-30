import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.OnEvent;
import com.geode.annotations.Protocol;
import com.geode.net.*;

@Protocol(value = "Test", scope = Protocol.Scope.QUERY)
public class MainCLI
{

	public static void main(String[] args) throws InterruptedException
	{
		Geode geode = new Geode("resources/geode.xml");

		UdpHandler handler = geode.launchUdpHandler("MyUdpClient");
		handler.send("Hi bro!!!");
	}

	@Inject
	public ClientProtocolHandler handler;
	
	@OnEvent(OnEvent.Event.INIT_OR_REBOOT)
	public void init()
	{
		handler.send(Query.simple("ping"));
	}

	@Control
	public void pong()
	{
		System.out.println("> pong");
	}
}
