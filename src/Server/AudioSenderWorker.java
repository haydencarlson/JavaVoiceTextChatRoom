package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class AudioSenderWorker extends Thread {
    private ArrayList<ServerClient> clients;
    private DatagramSocket clientConnection;
    private byte[] audioData;
    private String sentFromAddress;

    public AudioSenderWorker(DatagramSocket clientConnection, ArrayList<ServerClient> clients, byte[] audioData, String sentFromAddress) {
        this.clients = clients;
        this.audioData = audioData;
        this.clientConnection = clientConnection;
        this.sentFromAddress = sentFromAddress;
    }

    public void run() {
        for (int client = 0; client < clients.size(); client++) {
            try {
                ServerClient serverClient = clients.get(client);
                System.out.println(serverClient.clientAddress.getHostName());
                System.out.println(sentFromAddress);
                if (!serverClient.clientAddress.getHostAddress().equals(sentFromAddress)) {
                    // Build packet to send to server
                    DatagramPacket send_packet = new DatagramPacket(audioData, audioData.length, serverClient.clientAddress, serverClient.port);
                    // Send to server
                    clientConnection.send(send_packet);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
