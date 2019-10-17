package Client;

import java.net.DatagramPacket;

public class ClientProcessReceived extends Thread {
    private byte[] receiveData;
    private DatagramPacket receivePacket;
    private Client client;
    private AudioPlayer audioPlayer;
    ClientProcessReceived(Client client, byte[] receiveData, DatagramPacket receivePacket, AudioPlayer audioPlayer) {
        this.receiveData = receiveData;
        this.receivePacket = receivePacket;
        this.client = client;
        this.audioPlayer = audioPlayer;

    }

    @Override
    public void run() {
        audioPlayer.playAudio(receiveData);
    }

}
