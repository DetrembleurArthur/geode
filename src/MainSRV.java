import com.geode.net.Geode;
import com.geode.annotations.*;

@Protocol("Test")
public class MainSRV
{

	public static void main(String[] args)
	{
		Geode geode = new Geode("resources/geode.xml");

		geode.launchServer("MyServer");
	}
}
