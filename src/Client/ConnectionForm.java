package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.*;

// Handles connecting to server
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
            connection = new Socket(connectionAddress, 54540);

            if (connection.isConnected()) {

                // Create socket for sending
                udpSocket = new DatagramSocket();

                // Create receive socket
                DatagramSocket socketReceive = new DatagramSocket();


                byte[] buf =  ("/newuser/" + usernameText.getText()).getBytes();

                DatagramPacket send_packet = new DatagramPacket(buf, buf.length, connectionAddress, 54541);
                socketReceive.send(send_packet);

                // Handle server messages thread
                TCPServerConnection serverConnection = new TCPServerConnection(connection, this);
                serverConnection.start();

                System.out.println("Connected to server: " + connection.getInetAddress().getHostAddress() + ":" + connection.getPort());
                connected(serverConnection, socketReceive);
            }
        } catch (ConnectException e) {
           System.out.println("Unable to connect to server: " + connectionAddress + ":54540");
           connectButton.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void connected(TCPServerConnection serverConnection, DatagramSocket socketReceive) {
        // Start client
        client = new Client(connectionAddress, usernameText.getText(), serverConnection, socketReceive);
        client.start();
        // Hide connection form
        frame.setVisible(false);
    }

    public static void main(String[] args) {
        new ConnectionForm();
    }
}
