package Client;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {
	private JTextField userMessage;
	private JTextArea userMessages;
	private String username;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message;
	private String serverIP;
	private Socket connection;
	private int retryAttempts = 0;

	public Client(String host) {
		super("DIY Messenger Client");
		serverIP = host;
		username = "Anonymous";
		setupUI();
	}

	private void setupUI() {
		userMessage = new JTextField();
		userMessage.setEditable(false);
		userMessage.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sendMessage(e.getActionCommand());
					userMessage.setText("");
				}
			}
		);
		TopBar topbar = new TopBar(this);
		add(topbar, BorderLayout.NORTH);
		add(userMessage, BorderLayout.SOUTH);
		userMessages = new JTextArea();
		add(new JScrollPane(userMessages), BorderLayout.CENTER);
		setSize(450, 250);
		setVisible(true);
	}

	public void start() {
		try {
			connectToServer();
			setupStreams();
			whileChatting();
		} catch (EOFException e) {
			showMessage("\n Client connection closed");
		} catch (IOException e) {
			// Retry connection to server
			fieldEditable(userMessage, false);
			retryAttempts += 1;
			if (retryAttempts == 5) {
				showMessage("\n Unable to connect at this time");
				terminate();
			} else {
				this.start();
			}
		}
	}

	public void setUsername(String newUserName) {
		username = newUserName;
	}

	private void connectToServer() throws IOException {
		connection = new Socket(InetAddress.getByName(serverIP), 3000);
		showMessage("You are now connected! Say Hi.");
	}

	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
	}

	private void whileChatting () throws IOException {
		fieldEditable(userMessage, true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);
			} catch(ClassNotFoundException e) {
				showMessage("Error reading message");
			}
		} while(!message.equals("SERVER - END"));
	}

	private void terminate() {
		fieldEditable(userMessage, false);
		try {
			if (output != null) {
				output.close();
			}
			if (input != null) {
				input.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void sendMessage(String message) {
		try {
			String concatMessage = username + " : " + message;
			output.writeObject(concatMessage);
			output.flush();
			showMessage("\n" + concatMessage);
		} catch(IOException e) {
			userMessages.append("\n Error sending message");
		}
	}

	private void showMessage(final String message) {
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					userMessages.append(message);
				}
			}
		);
	}

	private void fieldEditable(JTextField field, final boolean ableToType) {
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					field.setEditable(ableToType);
				}
			}
		);
	}

}
