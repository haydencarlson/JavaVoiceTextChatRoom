package Client;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.Buffer;
import javax.sound.sampled.*;
import javax.swing.*;

public class Client extends JFrame {
	private JTextField userMessage;
	private JTextArea userMessages;
	private String username;
	private byte[] userMessageBuffer;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message;
	private String serverIP;
	private InetAddress address;
	private DatagramSocket connection;
	private AudioFormat audioFormat;
	private TargetDataLine targetDataLine;
	private SourceDataLine sourceDataLine;
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
			e -> {
				sendMessage(e.getActionCommand());
				userMessage.setText("");
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
			whileChatting();
			System.out.println(connection);
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

		// Connect to datagramsocket socket and join group
		address = InetAddress.getByName(serverIP);
		connection = new DatagramSocket();
		showMessage("You are now connected! Say Hi.");

		// Start thread that handles setting up sending
		Thread captureWorker = new ClientWorker(connection);
		captureWorker.start();
	}

	private void whileChatting () throws IOException {
		fieldEditable(userMessage, true);
		byte[] buf = new byte[1000];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);
		do {
			connection.receive(dp);
			message = new String(dp.getData());
			showMessage("\n" + message);
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
			byte[] userMessageBuffer = new byte[1000];
			userMessageBuffer = message.getBytes();
			DatagramPacket packet = new DatagramPacket(userMessageBuffer, userMessageBuffer.length, address, 3000);
			connection.send(packet);

		} catch(IOException e) {
			userMessages.append("\n Error sending message");
		}
	}

	private void showMessage(final String message) {
		SwingUtilities.invokeLater(
			() -> userMessages.append(message)
		);
	}

	private void fieldEditable(JTextField field, final boolean ableToType) {
		SwingUtilities.invokeLater(
			() -> field.setEditable(ableToType)
		);
	}

}
