package Server;

import com.sun.org.apache.xpath.internal.operations.Mult;
import sun.audio.AudioData;
import sun.audio.AudioDataStream;
import sun.audio.AudioPlayer;

import javax.sound.sampled.*;
import javax.xml.crypto.Data;
import javax.xml.transform.Source;
import java.io.*;
import java.net.*;

public class ServerWorker extends Thread {
	private final DatagramSocket connection;
	private InetAddress address;
	private Server server;
	private byte[] messageBuffer = new byte[1024];
	private String message;
	private SourceDataLine sourceDataLine;

	public ServerWorker(DatagramSocket socket, InetAddress address, Server server) {
		this.connection = socket;
		this.server = server;
		this.address = address;
	}

	public void run() {
		whileChatting();
	}

	private void whileChatting() {
		try {
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, getAudioFormat());
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(getAudioFormat());
			sourceDataLine.start();
			byte[] audioData = new byte[sourceDataLine.getBufferSize()];
			while (true) {
//				receiveMessage();
				receiveAudio(audioData);
			}
		} catch (LineUnavailableException e) {

		}
	}

	private void receiveMessage() {
		try {
			DatagramPacket receive_packet = new DatagramPacket(messageBuffer, messageBuffer.length);
			// Receive new packets
			connection.receive(receive_packet);

			// Display on server logs
			message = new String(receive_packet.getData());
			this.server.showMessage("\n" + message);

			// Send packet back to clients
			messageBuffer = message.getBytes();
			System.out.println(address);
			DatagramPacket send_packet = new DatagramPacket(messageBuffer, messageBuffer.length, address,3000);
			connection.send(send_packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void receiveAudio(byte[] audioData) {
		try {
			DatagramPacket receive_packet = new DatagramPacket(audioData, audioData.length);
			connection.receive(receive_packet);
			System.out.println("Received new audio packets: " + audioData.length);
			playAudio(audioData);
		} catch (Exception e) {

		}
	}

	private AudioFormat getAudioFormat() {
		float sampleRate = 44100.0F;
		int sampleSizeInBits = 16;
		int channels = 1;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false);
	}

	private void playAudio(byte[] data) {
		sourceDataLine.write(data, 0, data.length);
	}
}
