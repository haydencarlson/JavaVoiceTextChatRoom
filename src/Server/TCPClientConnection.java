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
            while (true) {
                RTSPBufferedWriter = new BufferedWriter(new OutputStreamWriter(this.connection.getOutputStream()));

                RTSPBufferedWriter.write("RTSP/1.0 200 OK");
                RTSPBufferedWriter.write("CSeq: ");
                RTSPBufferedWriter.write("Session: ");
                RTSPBufferedWriter.flush();
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
