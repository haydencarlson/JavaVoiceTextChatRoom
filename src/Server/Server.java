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
    private DatagramSocket connection;

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
            address = InetAddress.getByName("localhost");
            connection = new DatagramSocket(3000, address);
            showMessage("Server has started on: " + connection.getLocalSocketAddress());
            while(true) {
                try {
                    acceptNewConnections();
                } catch(EOFException e) {
                    showMessage("\n Server connection has been terminated");
                }
            }
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
