package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.*;

public class ConnectionForm {
    private JButton connectButton;
    private JTextField serverIpText;
    private JTextField usernameText;
    private JPanel connectionPanel;
    private InetAddress connectionAddress;
    private Socket connection;
    private JFrame frame;
    private DatagramSocket udpSocket;
    private volatile Client client;

    public ConnectionForm() {
        frame = new JFrame("Connect");
        frame.setContentPane(connectionPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        serverIpText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (serverIpText.getText().length() > 0 && usernameText.getText().length() > 0) {
                    connectButton.setEnabled(true);
                }
            }
        });

        usernameText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (serverIpText.getText().length() > 0 && usernameText.getText().length() > 0) {
                    connectButton.setEnabled(true);
                }
            }
        });
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectButton.setEnabled(false);
                System.out.println("Connecting to server...");
                connect();
            }
        });
    }

    private void connect() {
        try {
            // Initialize DatagramSocket socket
            connectionAddress = InetAddress.getByName(serverIpText.getText());

            // Init TCP Socket
            connection = new Socket("127.0.0.1", 54540);
            udpSocket = new DatagramSocket();
            TCPServerConnection serverConnection = new TCPServerConnection(connection, this);
            serverConnection.start();

            if (connection.isConnected()) {
                System.out.println("Connected to server: " + connection.getInetAddress().getHostAddress() + ":" + connection.getPort());
                connected(serverConnection);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Called by TCPSocketReceiver once connection is established
    public void connected(TCPServerConnection serverConnection) {
        // Start client
        client = new Client(connectionAddress, usernameText.getText(), serverConnection, udpSocket);
        client.start();
        // Hide connection form
        frame.setVisible(false);
    }

    public static void main(String[] args) {
        new ConnectionForm();
    }
}
