package ru.rinpolz.streamplayer.hotkeys;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ru.rinpolz.streamplayer.mainlogic.MainClass;
import ru.rinpolz.streamplayer.mainlogic.Settings;

public class KeyChooser {

	static boolean selected = false;

	public KeyChooser(int hotkeyid) {
		JFrame frame = new JFrame("->");
		JLabel label = new JLabel("Press some key...", SwingConstants.CENTER);

		frame.setSize(180, 85);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.add(label);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);

		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				selected = true;
				Settings.isOpened = false;
				Settings.keys[hotkeyid] = e.getKeyCode();
				MainClass.keyListener.initKeys();

				frame.removeKeyListener(this);
				frame.dispose();

				Settings.setSettings();
				
			}
		});
	}
}
