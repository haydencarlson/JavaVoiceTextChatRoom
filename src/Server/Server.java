package Server;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame {
    private JTextArea userMessages;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;

    public Server() {
        super("DIY Instant Messenger");
	    setupUI();
    }

	private void setupUI() {
		userMessages = new JTextArea();
		add(new JScrollPane(userMessages));
		setSize(300,150);
		setVisible(true);
	}

	public void start() {
    	try {
			server = new ServerSocket(3000, 100);
			while(true) {
				try {
					acceptNewConnections();
				} catch(EOFException e) {
					showMessage("\n Server connection has been terminated");
				}
			}
	    } catch(IOException e) {
		    e.printStackTrace();
	    }
	}

	private void acceptNewConnections() throws IOException {
    	connection = server.accept();
    	showMessage("\n User connected: " + connection.getInetAddress().getHostName());
    	ServerWorker worker = new ServerWorker(connection, output, input, this);
    	worker.start();
	}

	public void showMessage(final String message) {
    	SwingUtilities.invokeLater(
		    () -> userMessages.append(message)
	    );
	}


}
