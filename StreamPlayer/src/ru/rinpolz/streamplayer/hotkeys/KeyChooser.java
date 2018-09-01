package ru.rinpolz.streamplayer.hotkeys;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ru.rinpolz.streamplayer.listeners.SKeyListener;
import ru.rinpolz.streamplayer.mainlogic.MainClass;
import ru.rinpolz.streamplayer.mainlogic.Settings;

public class KeyChooser {

	public KeyChooser(int hotkeyid) {
		JDialog frame = new JDialog();
		JLabel label = new JLabel("Press some key...", SwingConstants.CENTER);

		frame.setUndecorated(true);
		frame.setSize(180, 85);
		frame.setResizable(false);
		frame.getContentPane().setBackground(Color.lightGray);
		frame.setLocationRelativeTo(Settings.mainframe);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.add(label);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);

		frame.addKeyListener(new SKeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {

				Settings.isOpened = false;

				System.out.println(KeyEvent.getKeyText(e.getKeyCode()));

				Settings.keys[hotkeyid] = e.getKeyCode();

				MainClass.keyListener.initKeys();

				frame.removeKeyListener(this);

				Settings.setSettings();
				Settings.mainframe.setAlwaysOnTop(true);
				Settings.mainframe.setAlwaysOnTop(false);
				Settings.mainframe.setEnabled(true);

				frame.dispose();

			}
		});
	}
}
