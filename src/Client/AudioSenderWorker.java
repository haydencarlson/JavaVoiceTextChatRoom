package Client;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class AudioSenderWorker extends Thread {
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private byte tempAudioBuffer[] = new byte[1024];
    private MulticastSocket connection;
    private Client client;
    private InetAddress address;
    private Mixer mixer;
    private DataLine.Info dataLineInfo;

    public AudioSenderWorker(MulticastSocket connection, Client client, InetAddress address) {
        this.connection = connection;
        this.client = client;
        this.address = address;
    }

    public void run() {
        getAudioDevice();
        sendAudio();
    }

    private void sendAudio () {
        try {
            // Get line
            targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);

            // Open the line ready to capture with specific audio format
            targetDataLine.open(audioFormat);

            // Start receiving data from line
            targetDataLine.start();

            // Create buffer to store received bytes
            byte[] data = new byte[targetDataLine.getBufferSize()];
            while (true) {

                // Read bytes from line
                targetDataLine.read(data, 0, data.length);

                // Build packet to send to server
                DatagramPacket send_packet = new DatagramPacket(data, data.length, address, 3000);

                // Send to server
                connection.send(send_packet);
            }
        } catch (IOException | LineUnavailableException e) {
            System.out.println(e);
        }
    }

    // Configures audio line to use for capturing
    private void getAudioDevice() {
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        audioFormat = getAudioFormat();
        dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

//        getSupportedFormats(TargetDataLine.class);

        // Hard coded to mixer 4
        mixer = AudioSystem.getMixer(mixerInfo[4]);
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
