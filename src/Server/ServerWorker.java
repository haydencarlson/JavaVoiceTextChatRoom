package Server;

import com.sun.org.apache.xpath.internal.operations.Mult;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;

public class ServerWorker extends Thread {
	private final DatagramSocket connection;
	private InetAddress address;
	private Server server;
	private byte[] buffer;
	private String message;

	public ServerWorker(DatagramSocket socket, InetAddress address, Server server) {
		this.connection = socket;
		this.server = server;
	}

	@Override
	public void run() {
		try {
			whileChatting();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void whileChatting() throws IOException {
		byte[] buffer = new byte[1000];
		DatagramPacket receive_packet = new DatagramPacket(buffer, buffer.length);
		while (true) {

			// Receive new packets
			connection.receive(receive_packet);
			message = new String(receive_packet.getData());

			// Send packet out
			buffer = message.getBytes();
			DatagramPacket send_packet = new DatagramPacket(buffer, buffer.length, receive_packet.getAddress(), receive_packet.getPort());
			connection.send(send_packet);

			this.server.showMessage("\n" + message);
		}
	}
}
