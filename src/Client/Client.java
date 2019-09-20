package Client;

import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;

public class Client extends JFrame {
	private JTextField userMessage;
	private JTextArea userMessages;
	private String username;
	private InetAddress connectionAddress;
	private TCPServerConnection connection;
	private DatagramSocket udpSocket;
	private DatagramSocket socketReceive;
	public Client(InetAddress connectionAddress, String username, TCPServerConnection connection, DatagramSocket udpSocket, DatagramSocket socketReceive) {
		super("DIY Messenger Client");
		this.username = username;
		this.connection = connection;
		this.connectionAddress = connectionAddress;
		this.udpSocket = udpSocket;
		this.socketReceive = socketReceive;
		setupUI();
	}

	private void setupUI() {
		userMessage = new JTextField();
		add(userMessage, BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		userMessages = new JTextArea();
		add(new JScrollPane(userMessages), BorderLayout.CENTER);
		setSize(450, 250);
		setVisible(true);
	}


	public void start() {
		// Start thread that handles sending audio
		Thread audioSenderWorker = new AudioSenderWorker(socketReceive, this, connectionAddress);
		audioSenderWorker.start();

		// Thread to handle receiving audio
		ClientAudioReceiverWorker audioReceiverWorker = new ClientAudioReceiverWorker(socketReceive);
		audioReceiverWorker.start();
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
