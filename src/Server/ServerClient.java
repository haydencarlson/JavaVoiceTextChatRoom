package Server;

import java.net.InetAddress;

public class ServerClient {
    public InetAddress clientAddress;
    public int port;
    public ServerClient(InetAddress clientAddress, int port) {
        this.clientAddress = clientAddress;
        this.port = port;
    }
}
