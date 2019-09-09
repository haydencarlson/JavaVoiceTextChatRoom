package Client;


import javax.sound.sampled.*;
import java.net.*;

public class ClientAudioReceiverWorker extends Thread {
    private final DatagramSocket connection;
    private SourceDataLine sourceDataLine;

    public ClientAudioReceiverWorker(DatagramSocket connection) {
        this.connection = connection;
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
                receiveAudio(audioData);
            }
        } catch (LineUnavailableException e) {

        }
    }

    private void receiveAudio(byte[] audioData) {
        try {
            DatagramPacket receive_packet = new DatagramPacket(audioData, audioData.length);
            connection.receive(receive_packet);
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
