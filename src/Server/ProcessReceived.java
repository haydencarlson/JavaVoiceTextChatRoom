package Server;

import java.net.DatagramPacket;

public class ProcessReceived extends Thread {
    private byte[] receiveData;
    private DatagramPacket receivePacket;
    private Server server;
    ProcessReceived(Server server, byte[] receiveData, DatagramPacket receivePacket) {
        this.receiveData = receiveData;
        this.receivePacket = receivePacket;
        this.server = server;
    }
    @Override
    public void run() {
        // Add new user or receive audio + broadcast, receive messages
        String receiveString = new String(receivePacket.getData());

        if (receiveString.contains("/newuser/")) {
            // Create new client
            ServerClient newServerClient = new ServerClient(receivePacket.getAddress(), receivePacket.getPort(), receiveString.split("/newuser/")[1]);

            // Add to array if it doesn't exist
            server.addNewClient(newServerClient);
        }

        // Emit packets to all connected clients
        this.server.sendToAllClients(receiveData, receivePacket.getAddress().getHostAddress(), receivePacket.getPort());
    }
}
