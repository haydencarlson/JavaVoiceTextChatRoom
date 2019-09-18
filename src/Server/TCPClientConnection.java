package Server;

import java.net.Socket;

public class TCPClientConnection extends Thread {
    private Socket connection;
    public TCPClientConnection(Socket connection) {
        this.connection = connection;
    }
    public void run() {


    }
}
