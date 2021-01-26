import com.geode.net.Geode;
import com.geode.net.Q;
import com.geode.annotations.*;

@Protocol("Test")
public class MainSRV
{

	public static void main(String[] args)
	{
		Geode geode = new Geode("resources/geode.xml");

		geode.launchServer("MyServer");
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
