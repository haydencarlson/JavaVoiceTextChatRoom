package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;

public class Server {
    private DatagramSocket uniCastSocket;
    private DatagramSocket receiveAudioSocket;
    private ServerSocket serverSocket;
    private volatile ArrayList<ServerClient> clients;

    public Server() {
        clients = new ArrayList<ServerClient>();
    }

    public void start() {
        try {
            receiveAudioSocket = new DatagramSocket(54541);
            ServerAudioReceiverWorker audioReceiverWorker = new ServerAudioReceiverWorker(receiveAudioSocket, this);
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
                ServerClient newServerClient = new ServerClient(connection.getInetAddress(), connection.getPort());
                clients.add(newServerClient);
                new TCPClientConnection(connection).start();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public void addNewClient(ServerClient serverClient) {
        clients.add(serverClient);
    }


    public void sendToAllClients(byte[] audioData, String sentFromAddress) {
        AudioSenderWorker audioSenderWorker = new AudioSenderWorker(receiveAudioSocket, this.clients, audioData, sentFromAddress);
        audioSenderWorker.start();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
