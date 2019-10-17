package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerReceivePackets extends Thread {
    private DatagramSocket connection;
    private Server server;

    public ServerReceivePackets(DatagramSocket socket, Server server) {
        this.connection = socket;
        this.server = server;
    }

    public void run() {
        while (true) {
            try {
                // Packet for receiving data
                byte[] receiveData = new byte[44100];

                // Build datagram packet to receive
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // Receive data
                connection.receive(receivePacket);
                System.out.println("Received new packet from: " + receivePacket.getAddress().getHostAddress());

                // Run ProcessReceived worker to handle new data
                new ServerProcessReceived(server, receiveData, receivePacket).start();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
