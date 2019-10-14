package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;

public class Client extends JFrame {
	private JTextField userMessage;
	private JTextArea userMessages;
	private JList userList;
	private DefaultListModel userListModel;
	private String username;
	private InetAddress connectionAddress;
	private TCPServerConnection connection;
	private DatagramSocket socketReceive;
	public Client(InetAddress connectionAddress, String username, TCPServerConnection connection, DatagramSocket socketReceive) {
		super("JavaVoiceTextChatRoom");
		this.username = username;
		this.connection = connection;
		this.connectionAddress = connectionAddress;
		this.socketReceive = socketReceive;
		setupUI();
	}

	private void setupUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 300);
		setVisible(true);

		// User message text field
		userMessage = new JTextField();
		add(userMessage, BorderLayout.SOUTH);

		// User messages box
		userMessages = new JTextArea();
		add(new JScrollPane(userMessages), BorderLayout.CENTER);

		// Online user list
		userListModel = new DefaultListModel();
		userListModel.addElement("Online Users");
		userList = new JList(userListModel);
		userList.setPreferredSize(new Dimension(150, 200));
		add(userList, BorderLayout.LINE_END);

		addListeners();
	}

	private void addListeners() {
		userMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connection.send();
			}
		});
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
