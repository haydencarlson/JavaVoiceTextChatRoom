package Client;

import Server.AudioReceiverWorker;

import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;

public class Client extends JFrame {
	private JTextField userMessage;
	private JTextArea userMessages;
	private String username;
	private String message;
	private String serverIP;
	private InetAddress connectionAddress;
	private DatagramSocket connection;

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


	public void setUsername(String newUserName) {
		username = newUserName;
	}

	public void start() {
		try {
			// Initialize DatagramSocket socket
			connectionAddress = InetAddress.getByName(serverIP);
			connection = new DatagramSocket();

			// Send packet to tell server there is a new client
			newClientConnection();

			// Start thread that handles sending audio
			Thread audioSenderWorker = new AudioSenderWorker(connection, this, connectionAddress);
			audioSenderWorker.start();

			// Thread to handle receiving audio
			ClientAudioReceiverWorker audioReceiverWorker = new ClientAudioReceiverWorker(connection);
			audioReceiverWorker.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	private void newClientConnection() {
		try {
		    String connectionString = "/c/";
			// Build packet to send to server
			DatagramPacket send_packet = new DatagramPacket(connectionString.getBytes(), connectionString.length(), connectionAddress, 3000);
			// Send to server
			connection.send(send_packet);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void sendMessage(String message) {
		try {
			// Create buffer to store message bytes
			byte[] userMessageBuffer = new byte[1024];

			// String to bytes
			userMessageBuffer = message.getBytes();

			// Build datagram packet to send to server
			DatagramPacket packet = new DatagramPacket(userMessageBuffer, userMessageBuffer.length, connectionAddress, 3001);

			// Send to server
			connection.send(packet);
		} catch(IOException e) {
			userMessages.append("\n Error sending message");
		}
	}

	public void showMessage(final String message) {
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
