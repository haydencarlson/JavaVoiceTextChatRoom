package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;

public class Server {
    private DatagramSocket udpSocket;
    private ServerSocket serverSocket;
    public volatile ArrayList<ServerClient> clients;

    public Server() {
        clients = new ArrayList<ServerClient>();
    }

    public void start() {
        try {
            // This is used for receiving and sending audio
            udpSocket = new DatagramSocket(54541);

            // Handles receiving packets from clients, and processing it
            ReceivePackets receivePackets = new ReceivePackets(udpSocket, this);
            receivePackets.start();

            // TCP Socket for session management
            serverSocket = new ServerSocket(54540);
            System.out.println("Server has started");

            // Loop to accept new TCP connections
            acceptNewConnections();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptNewConnections() {
        while(true) {
            try {
                Socket connection = serverSocket.accept();
                new TCPClientConnection(connection).start();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public void addNewClient(ServerClient serverClient) {
        // Look through client list
        boolean found = false;
        for (int i = 0; i < this.clients.size(); i++) {
            ServerClient client = this.clients.get(i);
            if (client.clientAddress.equals(serverClient.clientAddress) && client.port == serverClient.port) {
                found = true;
            }
        }
        // Add to list if it doesn't exist
        if (!found) {
            System.out.println("New client connected" + serverClient.clientAddress + " Port: " + serverClient.port);
            clients.add(serverClient);
        }
    }


    public void sendToAllClients(byte[] audioData, String sentFromAddress, int sentFromPort) {
        AudioSenderWorker audioSenderWorker = new AudioSenderWorker(udpSocket, this.clients, audioData, sentFromAddress, sentFromPort);
        audioSenderWorker.start();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
