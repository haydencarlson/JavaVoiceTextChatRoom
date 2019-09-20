package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;

public class Server {
    private DatagramSocket udpSocket;
    private ServerSocket serverSocket;
    private volatile ArrayList<ServerClient> clients;

    public Server() {
        clients = new ArrayList<ServerClient>();
    }

    public void start() {
        try {
            udpSocket = new DatagramSocket(54541);

//            NewUDPClient newUDPClient = new NewUDPClient(udpSocket, this);
//            newUDPClient.start();

            ServerAudioReceiverWorker audioReceiverWorker = new ServerAudioReceiverWorker(udpSocket, this);
            audioReceiverWorker.start();

            serverSocket = new ServerSocket(54540);
            System.out.println("Server has started");
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

        boolean found = false;
        for (int i = 0; i < this.clients.size(); i++) {
            ServerClient client = this.clients.get(i);
            if (client.clientAddress.equals(serverClient.clientAddress) && client.port == serverClient.port) {
                found = true;
            }
        }

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
