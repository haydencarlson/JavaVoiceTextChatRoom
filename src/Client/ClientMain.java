package Client;

import javax.swing.JFrame;

public class ClientMain {
	public static void main(String[] args) {
		Client client = new Client("192.168.1.67");
		client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.start();
	}
}
