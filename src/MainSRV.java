import com.geode.net.*;
import com.geode.annotations.*;

@Protocol("Test")
public class MainSRV
{

	public static void main(String[] args)
	{
		Geode geode = new Geode("resources/server.xml");

		geode.launchServer("MyServer");

	}
}
