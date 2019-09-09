package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;

public class Server extends JFrame {
    private JTextArea userMessages;
    private InetAddress address;
    private DatagramSocket uniCastSocket;
    private DatagramSocket receiveAudioSocket;
    private volatile ArrayList<ServerClient> clients;

    public Server() {
        super("DIY Instant Messenger");
        setupUI();
        clients = new ArrayList<ServerClient>();
    }

    private void setupUI() {
        userMessages = new JTextArea();
        add(new JScrollPane(userMessages));
        setSize(300,150);
        setVisible(true);
    }

    public void start() {
        try {
            uniCastSocket = new DatagramSocket(3000);
            receiveAudioSocket = new DatagramSocket(3001);
            NewConnectionWorker newConnectionWorker = new NewConnectionWorker(uniCastSocket, this);
            newConnectionWorker.start();
            AudioReceiverWorker audioReceiverWorker = new AudioReceiverWorker(receiveAudioSocket, this);
            audioReceiverWorker.start();
        } catch(SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewClient(ServerClient serverClient) {
        clients.add(serverClient);
        System.out.println(clients.size());
    }


    public void sendToAllClients(byte[] audioData, String sentFromAddress) {
        AudioSenderWorker audioSenderWorker = new AudioSenderWorker(uniCastSocket, this.clients, audioData, sentFromAddress);
        audioSenderWorker.start();
    }

    public void showMessage(final String message) {
        SwingUtilities.invokeLater(
            () -> userMessages.append(message)
        );
    }
}
