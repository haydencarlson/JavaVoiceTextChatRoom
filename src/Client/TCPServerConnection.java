package Client;

import java.io.*;
import java.net.Socket;

public class TCPServerConnection extends Thread {
    private Socket socket;
    private ObjectOutputStream output;
    private volatile ObjectInputStream input;
    private ConnectionForm connectionForm;
    private BufferedReader RTSPBufferReader;
    private BufferedWriter RTSPBufferWriter;

    public TCPServerConnection(Socket socket, ConnectionForm connectionForm) {
        this.socket = socket;
        this.connectionForm = connectionForm;
    }
    public void run() {
        setupStreams();
        receive();
    }

    private void receive() {
        while (true) {
            try {
                String received = RTSPBufferReader.readLine();
                System.out.println(received);
                return;
            } catch (NullPointerException | IOException e) {
                System.out.println(e);
            }
        }
    }

    private void setupStreams() {
        try {
            RTSPBufferReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            RTSPBufferWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void send() {
        try {
            RTSPBufferWriter.write("TEST");
            RTSPBufferWriter.newLine();
            RTSPBufferWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
