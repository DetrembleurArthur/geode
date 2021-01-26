import com.geode.net.Geode;
import com.geode.net.Q;
import com.geode.annotations.*;
import com.geode.net.UdpStream;

import javax.swing.*;

@Protocol("Test")
public class MainSRV
{

	public static void main(String[] args)
	{/*
		Geode geode = new Geode("resources/geode.xml");

		geode.launchServer("MyServer");*/

		String message = "Hello world!";

		UdpStream stream = new UdpStream("127.0.0.1", 1500, false);
		stream.send(message);
		message = stream.recv();
		JOptionPane.showMessageDialog(null, message);
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
