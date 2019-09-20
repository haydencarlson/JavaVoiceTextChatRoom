package Server;

import java.io.*;
import java.net.Socket;

public class TCPClientConnection extends Thread {
    private Socket connection;
    private static BufferedWriter RTSPBufferedWriter;
    private static BufferedReader RTSPBufferedReader;

    public TCPClientConnection(Socket connection) {
        this.connection = connection;
    }
    public void run() {
        try {
            RTSPBufferedReader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
            RTSPBufferedWriter = new BufferedWriter(new OutputStreamWriter(this.connection.getOutputStream()));
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
