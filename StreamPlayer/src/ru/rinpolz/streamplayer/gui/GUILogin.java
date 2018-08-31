package ru.rinpolz.streamplayer.gui;

import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ru.rinpolz.streamplayer.FormatedDox.PortDoc;
import ru.rinpolz.streamplayer.listeners.SKeyListener;
import ru.rinpolz.streamplayer.mainlogic.Client;
import ru.rinpolz.streamplayer.mainlogic.MainClass;
import ru.rinpolz.streamplayer.mainlogic.Server;
import ru.rinpolz.streamplayer.settingsIO.FileLoader;
import ru.rinpolz.streamplayer.util.Utils;

public class GUILogin extends JFrame {
	private static final long serialVersionUID = 5676639024889958052L;

	static JButton connect = new JButton("Connect");
	static JButton makehost = new JButton("Start Host");
	public JButton language_select = new JButton("Language");
	public JButton b_news = new JButton("?");

	static JTextField port = new JTextField();
	static JTextField ip = new JTextField();

	public JLabel l_port = new JLabel("Port:");
	public JLabel l_ip = new JLabel("IP:");
	public JLabel welcome = new JLabel("Welcome to revolution music stream system OPG-9000");
	public JLabel version = new JLabel("Version: " + Utils.VERSION);
	public JLabel status = new JLabel();
	public JLabel check = new JLabel();

	public GUILogin() {
		System.out.println("Init Login GUI...");

		this.setIconImage(MainClass.rl.login_icon);
		this.setSize(305, 150);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("StreamPlayer Auth");
		this.setLayout(null);

		this.add(welcome);
		welcome.setBounds(8, 0, 300, 25);
		welcome.setFont(Utils.LABEL_FONT);
		welcome.setToolTipText("I do not know why this is written here and what it means");

		this.add(check);
		check.setBounds(25, 25, 100, 25);

		this.add(version);
		version.setBounds(5, 100, 200, 25);
		version.setFont(Utils.LABEL_FONT);

		this.add(l_ip);
		l_ip.setBounds(45, 22, 100, 25);

		this.add(l_port);
		l_port.setBounds(35, 48, 100, 25);

		this.add(status);
		status.setBounds(30, 75, 150, 25);
		status.setFont(Utils.LABEL_FONT);
		
		this.add(language_select);
		language_select.setBounds(155, 98, 100, 20);

		this.add(b_news);
		b_news.setBounds(255, 98, 40, 20);
		b_news.addActionListener(e -> {
			JOptionPane.showMessageDialog(this,
					"Changelog on 1.9: " + 
					"\n-Удален лишний код, упрощена логика интерфейса" + 
					"\n-Продолжается работа над интерфейсом программы" + 
					"\n-Добавлено меню с метадатой файла"
					, "News?", JOptionPane.INFORMATION_MESSAGE);
		});

		this.add(port);
		port.setBounds(60, 50, 100, 22);
		port.setDocument(new PortDoc());

		this.add(ip);
		ip.setBounds(60, 25, 100, 22);
		ip.addKeyListener(new SKeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (ip.getText().length() >= 100) {
					e.consume();
					ip.setText(ip.getText().substring(0, 100));
					Utils.BeePBoop();
				}
			}
		});

		this.add(connect);
		connect.setBounds(188, 25, 90, 25);
		// >
		connect.addActionListener(e -> {
			if (checkCorrect()) {
				connect.setEnabled(false);
				MainClass.port = getPort();
				MainClass.ip = getIp();
				Client client = new Client();
				client.start();
				status.setText("Connecting to " + MainClass.ip + "...");
			}

		});

		this.add(makehost);
		makehost.setBounds(188, 50, 90, 25);
		makehost.addActionListener(e -> {
			if (checkCorrect()) {
				MainClass.ip = getIp();
				MainClass.port = getPort();
				Server server = new Server();
				FileLoader.saveSettings();
				server.start();
			}
		});

		ip.setText(MainClass.ip);
		port.setText(MainClass.port + "");
		this.setVisible(true);
		try {
			// Exception in thread "main" java.lang.ExceptionInInitializerError
			// Caused by: java.lang.IllegalStateException: Buffers have not been created
			this.createBufferStrategy(2);
		} catch (Exception e) {
			System.out.println("Ебаные буферы X2");
			e.printStackTrace();
		}

	}

	public void setButtonStatus(boolean bool) {
		connect.setEnabled(bool);
		makehost.setEnabled(bool);
		port.setEnabled(bool);
		ip.setEnabled(bool);
	}

	public int getPort() {
		return Integer.parseInt(port.getText());
	}

	public boolean checkCorrect() {
		try {
			if (Integer.parseInt(port.getText()) > 65535) {
				throw new Exception();
			}

			return true;
		} catch (Exception e) {
			status.setText("Enter the correct IP/Port!");
			return false;
		}
	}

	public void setPort(int porte) {
		port.setText(String.valueOf(port));
	}

	public String getIp() {
		return ip.getText();
	}

	public void setIp(String ipe) {
		ip.setText(ip + "");
	}
}