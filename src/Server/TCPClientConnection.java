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
            RTSPBufferedWriter = new BufferedWriter(new OutputStreamWriter(this.connection.getOutputStream()));
            RTSPBufferedReader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
            receive();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void receive() {
        try {
            String receivedLine;
            while ((receivedLine = RTSPBufferedReader.readLine()) != null) {
                System.out.println("New message received! " + receivedLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
