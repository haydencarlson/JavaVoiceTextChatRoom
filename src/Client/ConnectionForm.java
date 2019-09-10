package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectionForm {
    private JButton connectButton;
    private JTextField serverIpText;
    private JTextField usernameText;
    private JPanel connectionPanel;
    private InetAddress connectionAddress;
    private DatagramSocket connection;
    private JFrame frame;
    private ClientNewConnectionWorker clientNewConnectionWorker;
    private volatile Client client;
    private Timer connectionCheckTimer;

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
            connection = new DatagramSocket();
            String connectionString = "/c/";

            // Build packet to send to server
            DatagramPacket send_packet = new DatagramPacket(connectionString.getBytes(), connectionString.length(), connectionAddress, 3000);

            // Send to server
            connection.send(send_packet);

            clientNewConnectionWorker = new ClientNewConnectionWorker(connection, this);
            clientNewConnectionWorker.start();

            checkForConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Called by ClientNewConnectionWorker if packet is received from server
    public void connected() {

        // Try to cancel scheduled timer
        connectionCheckTimer.cancel();
        connectionCheckTimer.purge();

        // Stop thread looking for connection response
        clientNewConnectionWorker.interrupt();

        // Start client
        client = new Client(connectionAddress, usernameText.getText(), connection);
        client.start();

        // Hide connection form
        frame.setVisible(false);
    }

    // Checks for client connection after 5 seconds or re enables connect button
    private void checkForConnection() {
        connectionCheckTimer = new Timer();
        connectionCheckTimer.schedule(new TimerTask() {
            public void run() {
                if (client == null) {
                    System.out.println("Unable to connect");
                    connectButton.setEnabled(true);
                }
            }
        }, 5000);
    }

    public static void main(String[] args) {
        new ConnectionForm();
    }
}
