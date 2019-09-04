package Client;
 import javax.swing.JButton;
 import javax.swing.JTextField;
 import javax.swing.JPanel;
 import java.awt.*;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;

public class TopBar extends JPanel implements ActionListener {
	private JButton acceptUsername;
	private JTextField username;
	private Client client;
	public TopBar(Client client) {
		this.client = client;
		acceptUsername = new JButton();
		acceptUsername.setText("Change Username");
		acceptUsername.addActionListener(this);
		username = new JTextField();
		username.setText("Anonymous");
		username.setColumns(20);
		username.addActionListener(this);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(acceptUsername);
		add(username);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Change Username")) {
			client.setUsername(username.getText());
		}
	}
}
