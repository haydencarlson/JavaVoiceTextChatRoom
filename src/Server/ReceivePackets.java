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
                byte[] audioData = new byte[44100];
                DatagramPacket receive_packet = new DatagramPacket(audioData, audioData.length);
                connection.receive(receive_packet);
                System.out.println("Received new packet from: " + receive_packet.getAddress().getHostAddress());
                ServerClient newServerClient = new ServerClient(receive_packet.getAddress(), receive_packet.getPort());
                server.addNewClient(newServerClient);
                this.server.sendToAllClients(audioData, receive_packet.getAddress().getHostAddress(), receive_packet.getPort());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
