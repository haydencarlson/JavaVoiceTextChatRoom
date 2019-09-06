package Server;

import com.sun.org.apache.xpath.internal.operations.Mult;
import sun.audio.AudioData;
import sun.audio.AudioDataStream;
import sun.audio.AudioPlayer;

import javax.sound.sampled.*;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;

public class ServerWorker extends Thread {
	private final DatagramSocket connection;
	private InetAddress address;
	private Server server;
	private byte[] buffer = new byte[1024];
	private String message;
	private SourceDataLine sourceDataLine;

	public ServerWorker(DatagramSocket socket, InetAddress address, Server server) {
		this.connection = socket;
		this.server = server;
		this.address = address;
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
		while (true) {
			DatagramPacket receive_packet = new DatagramPacket(buffer, buffer.length);

			// Receive new packets
			connection.receive(receive_packet);
			message = new String(receive_packet.getData());
			playAudio(receive_packet.getData());
			this.server.showMessage("\n" + message);
			try {
				// Send packet out
				buffer = message.getBytes();
				System.out.println(address);
				DatagramPacket send_packet = new DatagramPacket(buffer, buffer.length, address,3000);
				connection.send(send_packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private AudioFormat getAudioFormat() {
		float sampleRate = 44100.0F;
		int sampleSizeInBits = 16;
		int channels = 1;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false);
	}

	private void playAudio(byte[] message) {
		try {
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, getAudioFormat());
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(getAudioFormat());
			sourceDataLine.start();
//			sourceDataLine.write(message, 0, buffer.length);
			AudioData audioData = new AudioData(message);
			AudioDataStream audioStream = new AudioDataStream(audioData);
			AudioPlayer.player.start(audioStream);
		} catch (LineUnavailableException e) {
			System.out.println(e);
		}
	}
}
