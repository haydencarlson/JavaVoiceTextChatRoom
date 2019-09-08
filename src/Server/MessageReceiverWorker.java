package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MessageReceiverWorker extends Thread {
    private DatagramSocket uniCastConnection;
    private InetAddress address;
    private Server server;
    private String message;

    public MessageReceiverWorker(DatagramSocket uniCastConnection, InetAddress address, Server server) {
        this.uniCastConnection = uniCastConnection;
        this.address = address;
        this.server = server;
    }

    public void run() {
        receiveMessages();
    }

    private void receiveMessages() {
        byte[] messageBuffer = new byte[1024];
        while (true) {
            try {
                DatagramPacket receive_packet = new DatagramPacket(messageBuffer, messageBuffer.length);
                // Receive new packets
                uniCastConnection.receive(receive_packet);

                // Display on server logs
                message = new String(receive_packet.getData());
                this.server.showMessage("\n" + message);

                // Send packet back to client
                messageBuffer = message.getBytes();
                DatagramPacket send_packet = new DatagramPacket(messageBuffer, messageBuffer.length, receive_packet.getAddress(), receive_packet.getPort());
                uniCastConnection.send(send_packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
