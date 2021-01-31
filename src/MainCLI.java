import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.OnEvent;
import com.geode.annotations.Protocol;
import com.geode.net.*;

import javax.swing.*;
import java.util.Scanner;

@Protocol(value = "Test")
public class MainCLI
{
	private static String group;

	public static void main(String[] args) throws InterruptedException
	{
		Geode geode = new Geode("resources/geode.xml");

		Scanner scanner = new Scanner(System.in);
		ClientProtocolHandler handler = geode.launchClient("MyClient").getHandlerSafe();
		while (true)
		{
			System.out.print("Message:" + group + "> ");
			String msg = scanner.nextLine();
			handler.send(Query.topicNotifyOthers(group).pack(msg));
		}
	}

	@Inject
	public ClientProtocolHandler handler;
	
	@OnEvent
	public void init()
	{
		group = JOptionPane.showInputDialog("Subscribe to a group:");
		handler.subscribeTopic(group);
		handler.getDynamicConstrols().put(group, args -> {
			System.out.println("Message : " + args);
			return null;
		});
	}
}
