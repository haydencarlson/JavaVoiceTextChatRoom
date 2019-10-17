package Client;

import Server.Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientReceivePackets extends Thread{
    private DatagramSocket connection;
    private Client client;
    private AudioPlayer audioPlayer;
    public ClientReceivePackets(DatagramSocket socket, Client client) {
        this.connection = socket;
        this.client = client;
        this.audioPlayer = new AudioPlayer();
    }
    public void run() {
        while (true) {
            try {
                // Packet for receiving data
                byte[] receiveData = new byte[44100];

                // Build datagram packet to receive
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // Receive data
                connection.receive(receivePacket);

                // Run ProcessReceived worker to handle new data
                new ClientProcessReceived(client, receiveData, receivePacket, audioPlayer).start();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
