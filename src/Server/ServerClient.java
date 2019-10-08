package Server;

import java.net.InetAddress;

// ServerClient is an instance of a connected client to the server which is used to send data to.
public class ServerClient {
    public InetAddress clientAddress;
    public int port;
    public String username;
    public ServerClient(InetAddress clientAddress, int port, String username) {
        this.clientAddress = clientAddress;
        this.port = port;
        this.username = username;
    }
}
