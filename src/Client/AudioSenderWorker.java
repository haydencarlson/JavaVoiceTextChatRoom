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
    private Mixer mixer;
    private DataLine.Info dataLineInfo;
    private ObjectInputStream input;
    private ObjectOutputStream output;

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

                System.out.println("Sending audio packet: " + send_packet.getLength());

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

    // Looks for mixer line supported formats
    public void getSupportedFormats(Class<?> dataLineClass) {
        float sampleRates[] = {(float) 8000.0, (float) 16000.0, (float) 44100.0};
        int channels[] = {1, 2};
        int bytesPerSample[] = {2};

        AudioFormat format;
        DataLine.Info lineInfo;

        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            for (int a = 0; a < sampleRates.length; a++) {
                for (int b = 0; b < channels.length; b++) {
                    for (int c = 0; c < bytesPerSample.length; c++) {
                        format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                            sampleRates[a], 8 * bytesPerSample[c], channels[b], bytesPerSample[c],
                            sampleRates[a], false);
                        lineInfo = new DataLine.Info(dataLineClass, format);
                        if (AudioSystem.isLineSupported(lineInfo)) {
                            if (AudioSystem.getMixer(mixerInfo).isLineSupported(lineInfo)) {
                                System.out.println(mixerInfo.getName());
                                System.out.println(format);
                            }
                        }
                    }
                }
            }
        }
    }
}
