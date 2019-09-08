package Server;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class Server extends JFrame {
    private JTextArea userMessages;
    private InetAddress address;
    private MulticastSocket multiCastConnection;
    private DatagramSocket uniCastConnection;

    public Server() {
        super("DIY Instant Messenger");
        setupUI();
    }

    private void setupUI() {
        userMessages = new JTextArea();
        add(new JScrollPane(userMessages));
        setSize(300,150);
        setVisible(true);
    }

    public void start() {
        try {

            // Set up multicast group
            address = InetAddress.getByName("224.0.0.1");
            multiCastConnection = new MulticastSocket(3000);
            multiCastConnection.setReuseAddress(true);
            multiCastConnection.joinGroup(address);
            showMessage("\nMulticast has started on: " + multiCastConnection.getLocalSocketAddress());

            // Set up unicast connection
            uniCastConnection = new DatagramSocket(3001);
            showMessage("\nUnicast has started on: " + uniCastConnection.getLocalSocketAddress());
            acceptNewConnections();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptNewConnections() throws IOException {
        AudioReceiverWorker audioReceiverWorker = new AudioReceiverWorker(multiCastConnection, address, this);
        audioReceiverWorker.start();

        MessageReceiverWorker messageReceiverWorker = new MessageReceiverWorker(uniCastConnection, address, this);
        messageReceiverWorker.start();
    }

    public void showMessage(final String message) {
        SwingUtilities.invokeLater(
            () -> userMessages.append(message)
        );
    }
}
