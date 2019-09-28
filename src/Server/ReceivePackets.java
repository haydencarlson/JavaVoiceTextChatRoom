package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceivePackets extends Thread {
    private DatagramSocket connection;
    private Server server;

    public ReceivePackets(DatagramSocket socket, Server server) {
        this.connection = socket;
        this.server = server;
    }

    public void run() {
        while (true) {
            try {
                // Packet for receiving data
                byte[] audioData = new byte[44100];

                // Build datagram packet to receive
                DatagramPacket receive_packet = new DatagramPacket(audioData, audioData.length);

                // Receive data
                connection.receive(receive_packet);
                System.out.println("Received new packet from: " + receive_packet.getAddress().getHostAddress());

                // Create new client
                ServerClient newServerClient = new ServerClient(receive_packet.getAddress(), receive_packet.getPort());

                // Add to array if it doesnt exist
                server.addNewClient(newServerClient);

                // Emit packets to all connected clients
                this.server.sendToAllClients(audioData, receive_packet.getAddress().getHostAddress(), receive_packet.getPort());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
