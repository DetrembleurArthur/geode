import javax.swing.JOptionPane;

import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.OnEvent;
import com.geode.annotations.Protocol;
import com.geode.net.*;
import com.geode.net.Q.Category;

import java.util.Scanner;

@Protocol("Test")
public class MainCLI
{

	public static void main(String[] args) throws InterruptedException
	{
		Geode geode = new Geode("resources/geode.xml");

		Client client = geode.launchClient("MyClient");
		
		while(true)
		{
			String message = new Scanner(System.in).next();
			if(message.equalsIgnoreCase("end")) client.getHandlerSafe().unsubscribe("message");
			else client.getHandlerSafe().send(Q.notifyOthers("message").pack(message));
		}
	}
	
	@Inject
	public ClientProtocolHandler handler;
	
	@OnEvent
	public void init()
	{
		handler.subscribe("message");
	}

	@Control(type = Control.Type.CLIENT_CLIENTS)
	public void message(String message)
	{
		System.out.println("Message receive: " + message);
	}
}
