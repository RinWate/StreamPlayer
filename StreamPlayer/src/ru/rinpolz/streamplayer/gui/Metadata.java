package ru.rinpolz.streamplayer.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import ru.rinpolz.streamplayer.mainlogic.Server;

public class Metadata extends JFrame {
	private static final long serialVersionUID = 228988;

	public Metadata() {
		init();
		showData();
	}
	
	public void init() {
		this.setTitle("Metadata");
		this.setSize(220, 305);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Server.gui.b_metadata.setEnabled(true);
			}
		});
		
		this.setVisible(false);
	}
	
	public void showData() {
		this.setLocation(Server.gui.getLocation().x - 215, Server.gui.getLocation().y);
		this.setVisible(true);
		Server.gui.b_metadata.setEnabled(false);
	}
}
