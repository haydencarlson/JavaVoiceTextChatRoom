package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MessageReceiverWorker extends Thread {
    private DatagramSocket uniCastConnection;
    private Client client;
    private String message;
    private InetAddress address;

    public MessageReceiverWorker(DatagramSocket uniCastConnection, Client client, InetAddress address) {
        this.uniCastConnection = uniCastConnection;
        this.client = client;
        this.address = address;
    }
    public void run() {
        receiveMessages();
    }

    private void receiveMessages () {

        // Create buffer to store received message
        byte[] buf = new byte[1024];
        try {
            while (true) {

                // Create a packet to store received data
                DatagramPacket dp = new DatagramPacket(buf, buf.length);

                // Receive bytes from socket
                uniCastConnection.receive(dp);

                // Convert bytes to string to be displayed
                message = new String(dp.getData());

                // Display message on client
                this.client.showMessage("\n" + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
