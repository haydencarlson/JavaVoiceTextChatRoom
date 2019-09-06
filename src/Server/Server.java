package Server;

import com.sun.org.apache.xpath.internal.operations.Mult;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class Server extends JFrame {
    private JTextArea userMessages;
    private InetAddress address;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private MulticastSocket connection;

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
            address = InetAddress.getByName("224.0.0.1");
            connection = new MulticastSocket(3000);
            connection.setReuseAddress(true);
            connection.joinGroup(address);
            showMessage("Server has started on: " + connection.getLocalSocketAddress());
            acceptNewConnections();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptNewConnections() throws IOException {
        ServerWorker worker = new ServerWorker(connection, address, this);
        worker.start();
    }

    public void showMessage(final String message) {
        SwingUtilities.invokeLater(
            () -> userMessages.append(message)
        );
    }
}
