import com.geode.annotations.Control;
import com.geode.annotations.Inject;
import com.geode.annotations.OnEvent;
import com.geode.annotations.Protocol;
import com.geode.net.*;

import javax.swing.*;

@Protocol("Test")
public class MainCLI
{

	public static void main(String[] args)
	{/*
		Geode geode = new Geode("resources/geode.xml");

		geode.launchClient("MyClient");
		geode.launchClient("MyClient");
*/
		UdpStream stream = new UdpStream("127.0.0.1", 1500, true);
		String message = stream.recv();
		JOptionPane.showMessageDialog(null, message);
		stream.send("ACK");

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
