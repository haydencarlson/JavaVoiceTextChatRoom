package Server;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.ArrayList;

public class AudioSenderWorker extends Thread {
    private ArrayList<ServerClient> clients;
    private DatagramSocket clientConnection;
    private byte[] audioData;

    public AudioSenderWorker(DatagramSocket clientConnection, ArrayList<ServerClient> clients, byte[] audioData) {
        this.clients = clients;
        this.audioData = audioData;
        this.clientConnection = clientConnection;
    }

    public void run() {
        for (int client = 0; client < clients.size(); client++) {
            try {
                ServerClient serverClient = clients.get(client);
                // Build packet to send to server
                DatagramPacket send_packet = new DatagramPacket(audioData, audioData.length, serverClient.clientAddress, serverClient.port);
                // Send to server
                clientConnection.send(send_packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
