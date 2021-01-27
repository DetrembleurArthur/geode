import com.geode.admin.ConsumerFrame;
import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.OnEvent;
import com.geode.annotations.Protocol;
import com.geode.net.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;

@Protocol("Test")
public class MainCLI
{

	public static void main(String[] args) throws InterruptedException
	{
		Geode geode = new Geode("resources/geode.xml");

		Client client = geode.launchClient("MyClient");

		ConsumerFrame frame = new ConsumerFrame();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);

		((MainCLI)client.getHandlerSafe().getProtocol()).frame = frame;
		System.err.println(frame);
	}


	private ConsumerFrame frame;

	@Inject
	public ClientProtocolHandler handler;
	
	@OnEvent
	public void init()
	{
		handler.subscribeTopic("message");
	}

	@Control(type = Control.Type.TOPIC)
	public void message(String message)
	{
		System.out.println("Message receive: " + message);
		SwingUtilities.invokeLater(() -> {
			frame.textArea1.append(message + "\n");
		});
	}
}
