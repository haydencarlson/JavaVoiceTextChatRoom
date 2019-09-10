package Client;

import Server.ServerClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientNewConnectionWorker extends Thread {
    private DatagramSocket connection;
    private ConnectionForm connectionForm;
    public ClientNewConnectionWorker(DatagramSocket connection, ConnectionForm connectionForm) {
        this.connection = connection;
        this.connectionForm = connectionForm;
    }
    public void run() {
        byte[] connectionData = new byte[1024];
        while (true) {
            try {
                DatagramPacket receive_packet = new DatagramPacket(connectionData, connectionData.length);
                connection.receive(receive_packet);
                String connectionString = new String(receive_packet.getData());
                if (connectionString.contains("/c/")) {
                    connectionForm.connected();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
