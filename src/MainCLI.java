import javax.swing.JOptionPane;

import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.OnEvent;
import com.geode.annotations.Protocol;
import com.geode.net.*;
import com.geode.net.Q.Category;

@Protocol("Test")
public class MainCLI
{

	public static void main(String[] args)
	{
		Geode geode = new Geode("resources/geode.xml");

		Client client = geode.launchClient("MyClient");
		
		while(true)
		{
			client.getHandlerSafe().send(Q.simple("mytopic").setCategory(Category.TOPIC_NOTIFY)
					.pack(JOptionPane.showInputDialog("Message: ")));
		}


	}
	
	@Inject
	public ClientProtocolHandler handler;
	
	@OnEvent
	public void init()
	{
		handler.subscribe("mytopic", (a) -> {
			JOptionPane.showMessageDialog(null, a.get(0));
		});
	}
}
