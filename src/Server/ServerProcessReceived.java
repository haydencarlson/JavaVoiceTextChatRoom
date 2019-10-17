package Server;

import java.net.DatagramPacket;

public class ServerProcessReceived extends Thread {
    private byte[] receiveData;
    private DatagramPacket receivePacket;
    private Server server;
    ServerProcessReceived(Server server, byte[] receiveData, DatagramPacket receivePacket) {
        this.receiveData = receiveData;
        this.receivePacket = receivePacket;
        this.server = server;
    }
    @Override
    public void run() {
        // Add new user or receive audio + broadcast, receive messages
        String receiveString = new String(receivePacket.getData());

        // Handle type of data here
        // If Audio use audio sender worker
        // If new connection send message to all clients with new connected user
        // If message, send message to all clients with new message
        if (receiveString.contains("/newuser/")) {
            // Create new client
            ServerClient newServerClient = new ServerClient(receivePacket.getAddress(), receivePacket.getPort(), receiveString.split("/newuser/")[1]);

            // Add to array if it doesn't exist
            server.addNewClient(newServerClient);
        }

        // Emit packets to all connected clients
        // This can probably be removed and call appropriate threads to handle sending data to clients
        // there is access to server.clients in here
        this.server.sendToAllClients(receiveData, receivePacket.getAddress().getHostAddress(), receivePacket.getPort());
    }
}
