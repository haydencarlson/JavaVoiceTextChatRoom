package Client;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class AudioSenderWorker extends Thread {
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private byte tempAudioBuffer[] = new byte[1024];
    private DatagramSocket connection;
    private Client client;
    private InetAddress address;

    public AudioSenderWorker(DatagramSocket connection, Client client, InetAddress address) {
        this.connection = connection;
        this.client = client;
        this.address = address;
    }

    public void run() {
        sendAudio();
    }

    private void sendAudio () {
        try {

            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(getAudioFormat());
            targetDataLine.start();

            // Create buffer to store received bytes
            byte[] data = new byte[targetDataLine.getBufferSize()];
            while (true) {

                // Read bytes from line
                targetDataLine.read(data, 0, data.length);

                // Build packet to send to server
                DatagramPacket send_packet = new DatagramPacket(data, data.length, address, 3001);

                // Send to server
                connection.send(send_packet);
            }
        } catch (IOException | LineUnavailableException e) {
            System.out.println(e);
        }
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 44100.0F;
        int sampleSizeInBits = 16;
        int channels = 1;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false);
    }
}
