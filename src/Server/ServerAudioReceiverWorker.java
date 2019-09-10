package Server;

import java.net.*;

public class ServerAudioReceiverWorker extends Thread {
	private final DatagramSocket connection;
	private Server server;

	public ServerAudioReceiverWorker(DatagramSocket socket, Server server) {
		this.connection = socket;
		this.server = server;
	}

	public void run() {
		whileChatting();
	}

	private void whileChatting() {
		while (true) {
			receiveAudio();
		}
	}

	private void receiveAudio() {
		try {
			byte[] audioData = new byte[44100];
			DatagramPacket receive_packet = new DatagramPacket(audioData, audioData.length);
			connection.receive(receive_packet);
			this.server.sendToAllClients(audioData, receive_packet.getAddress().getHostAddress());
		} catch (Exception e) {

		}
	}
}
