import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.OnEvent;
import com.geode.annotations.Protocol;
import com.geode.net.*;

@Protocol("Test")
public class MainCLI
{

	public static void main(String[] args)
	{
		Geode geode = new Geode("resources/geode.xml");

		geode.launchClient("MyClient");


	}
	
	@Inject
	public ProtocolHandler handler;
	
	@OnEvent
	public void init()
	{
		handler.send(Q.simple("ping").pack(1));
	}
	
	@Control
	public Q pong(Integer n) throws InterruptedException
	{
		System.out.println("pong : " + n);
		n++;
		Thread.sleep(500);
		return Q.simple("ping").pack(n);
	}
}
