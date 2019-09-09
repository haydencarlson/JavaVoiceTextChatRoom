package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class NewConnectionWorker extends Thread {
    private DatagramSocket connection;
    private Server server;

    public NewConnectionWorker(DatagramSocket connection, Server server) {
        this.connection = connection;
        this.server = server;
    }

    public void run() {
        byte[] connectionData = new byte[1024];
        while (true) {
            try {
                DatagramPacket receive_packet = new DatagramPacket(connectionData, connectionData.length);
                connection.receive(receive_packet);
                String connectionString = new String(receive_packet.getData());
                if (connectionString.contains("/c/")) {
                    ServerClient newServerClient = new ServerClient(receive_packet.getAddress(), receive_packet.getPort());
                    this.server.showMessage("\n New user connected: " + receive_packet.getAddress());
                    this.server.addNewClient(newServerClient);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
