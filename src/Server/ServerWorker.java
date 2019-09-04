package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerWorker extends Thread {
	private final Socket clientConnection;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Server server;
	private String message;

	public ServerWorker(Socket clientConnection, ObjectOutputStream output, ObjectInputStream input, Server server) {
		this.clientConnection = clientConnection;
		this.output = output;
		this.input = input;
		this.server = server;
	}

	@Override
	public void run() {
		try {
			setupStreams();
			whileChatting();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(clientConnection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(clientConnection.getInputStream());
	}

	private void whileChatting() throws IOException {
		do {
			try {
				message = (String) input.readObject();
				this.server.showMessage("\n" + message);
			} catch(ClassNotFoundException classNotFoundException) {
				this.server.showMessage("\n Error receiving message");
			}
		} while(!message.equals("CLIENT - END"));
	}
}
