package Client;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientWorker extends Thread {
    private AudioFormat audioFormat;
    private DatagramSocket connection;
    private String message;
    public ClientWorker(DatagramSocket connection) {
        this.connection = connection;
    }
    public void run() {
//        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
//        System.out.println(mixerInfo[0].getName());
//        audioFormat = getAudioFormat();
//        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
//        Mixer mixer = AudioSystem.getMixer(mixerInfo[0]);
    }

    private void whileChatting() throws IOException {
        while (true) {
            byte[] receiveBuffer = new byte[1000];
            DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);

            connection.receive(packet);
            message = new String(packet.getData());
//            this.server.showMessage("\n" + message);
        }
    }
//    private AudioFormat getAudioFormat() {
//        float sampleRate = 8000.0F;
//        int sampleSizeInBits = 16;
//        int channels = 1;
//        return new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false);
//    }
}
