import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.OnEvent;
import com.geode.annotations.Protocol;
import com.geode.net.Client;
import com.geode.net.Geode;
import com.geode.net.ProtocolHandler;
import com.geode.net.Q;

@Protocol(name="Test")
public class MainCLI
{

	public static void main(String[] args)
	{
		Geode geode = new Geode();
		Client client = new Client("127.0.0.1", 6666);
		client.setProtocolClass(MainCLI.class);
		geode.buildClient(client);
		client.run();
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
