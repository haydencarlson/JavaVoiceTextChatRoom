package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class NewUDPClient extends Thread {
    private DatagramSocket udpSocket;
    private Server server;

    public NewUDPClient(DatagramSocket udpSocket, Server server) {
        this.udpSocket = udpSocket;
        this.server = server;
    }
    public void run() {
        while (true) {
            try {
                byte[] packetData = new byte[1024];
                DatagramPacket receive_packet = new DatagramPacket(packetData, packetData.length);
                udpSocket.receive(receive_packet);
                ServerClient newServerClient = new ServerClient(receive_packet.getAddress(), receive_packet.getPort());
                server.addNewClient(newServerClient);
                System.out.println("Received new packet from: " + receive_packet.getAddress().getHostAddress() + "  " + receive_packet.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
