package ru.rinpolz.streamplayer.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Debug {
	
	public static JFrame mainframe = new JFrame("Debug Info");
	public static JLabel l_debug = new JLabel();

	public Debug() {
		mainframe.setSize(200, 100);
		mainframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainframe.setResizable(false);
		mainframe.setLayout(null);
		mainframe.setLocationRelativeTo(null);
		mainframe.setVisible(false);
		
		mainframe.add(l_debug);
		l_debug.setBounds(3, -15, 110, 100);
	}
	
	public void startDebug(boolean isDebug) {
		if (isDebug) {
			mainframe.setVisible(true);
		} else {
			mainframe.dispose();
		}
	}
}
