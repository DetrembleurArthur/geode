import com.geode.net.Geode;
import com.geode.net.ProtocolHandler;
import com.geode.net.Q;
import com.geode.net.Server;
import com.geode.annotations.*;

@Protocol(name = "Test")
public class MainSRV
{

	public static void main(String[] args)
	{
		Geode geode = new Geode();
		Server server = new Server("127.0.0.1", 50, 6666);
		server.registerProtocolClass(MainSRV.class);
		geode.buildServer(server);
		server.start();
	}
	
	@Control
	public Q ping(Integer n) throws InterruptedException
	{
		System.out.println("ping: " + n);
		n++;
		Thread.sleep(500);
		return Q.simple("pong").pack(n);
	}

}
