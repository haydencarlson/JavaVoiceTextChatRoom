package Server;

import javax.sound.sampled.*;
import java.lang.annotation.Target;
import java.net.*;

public class AudioReceiverWorker extends Thread {
	private final DatagramSocket connection;
	private Server server;
	private SourceDataLine sourceDataLine;

	public AudioReceiverWorker(DatagramSocket socket, Server server) {
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
			System.out.println("Received new audio packets: " + audioData.length);
			this.server.sendToAllClients(audioData);
		} catch (Exception e) {

		}
	}
}
