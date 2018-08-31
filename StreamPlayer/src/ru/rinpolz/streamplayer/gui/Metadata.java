package ru.rinpolz.streamplayer.gui;

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
		
		this.setVisible(false);
	}
	
	public void showData() {
		Server.gui.b_metadata.setEnabled(false);
		this.setVisible(true);
	}
}
