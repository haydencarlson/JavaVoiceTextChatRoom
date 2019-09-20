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
    private int sentFromPort;

    public AudioSenderWorker(DatagramSocket clientConnection, ArrayList<ServerClient> clients, byte[] audioData, String sentFromAddress, int sentFromPort) {
        this.clients = clients;
        this.audioData = audioData;
        this.clientConnection = clientConnection;
        this.sentFromAddress = sentFromAddress;
        this.sentFromPort = sentFromPort;
    }

    public void run() {
        for (int client = 0; client < clients.size(); client++) {
            try {
                ServerClient serverClient = clients.get(client);
                if (!serverClient.clientAddress.getHostAddress().equals(sentFromAddress)
                    || serverClient.clientAddress.getHostAddress().equals(sentFromAddress)
                    && serverClient.port != sentFromPort ) {
                    // Build packet to send to server
                    DatagramPacket send_packet = new DatagramPacket(audioData, audioData.length, serverClient.clientAddress, serverClient.port);
                    // Send to server
                    System.out.println("Sending audio packet to " + serverClient.clientAddress + "port: " + serverClient.port);
                    clientConnection.send(send_packet);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;
    }
}

