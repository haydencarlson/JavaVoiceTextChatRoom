package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MessageReceiverWorker extends Thread {
    private MulticastSocket connection;
    private Client client;
    private String message;
    private InetAddress address;

    public MessageReceiverWorker(MulticastSocket connection, Client client, InetAddress address) {
        this.connection = connection;
        this.client = client;
        this.address = address;
    }
    public void run() {
        while (true) {
            try {
                receiveMessages();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    private void receiveMessages () throws IOException {
        byte[] buf = new byte[1000];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);
        connection.receive(dp);
        message = new String(dp.getData());
        this.client.showMessage("\n" + message);
    }
}
