package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPServerConnection extends Thread {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ConnectionForm connectionForm;

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
                String received = (String) input.readObject();
            } catch (ClassNotFoundException | IOException e) {
                System.out.println(e);
            }
        }
    }

    private void setupStreams() {
        try {
            output = new ObjectOutputStream(this.socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
