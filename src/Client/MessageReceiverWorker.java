package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

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
        byte[] buf = new byte[1024];
        try {
            while (true) {
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                uniCastConnection.receive(dp);
                message = new String(dp.getData());
                this.client.showMessage("\n" + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
