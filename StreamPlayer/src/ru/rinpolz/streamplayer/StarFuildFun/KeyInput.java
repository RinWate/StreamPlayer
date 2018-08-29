package ru.rinpolz.streamplayer.StarFuildFun;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class KeyInput extends JComponent {
	private static final long serialVersionUID = 1L;
	boolean[] keymap = new boolean[256];

	public KeyInput() {

		for (int i = 0; i < keymap.length; i++) {

			final int KEY_KODE = i;

			getInputMap().put(KeyStroke.getKeyStroke(i, 0, false), i * 2);
			getActionMap().put(i * 2, new AbstractAction() {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent arg0) {

					keymap[KEY_KODE] = true;
				}
			});

			getInputMap().put(KeyStroke.getKeyStroke(i, 0, true), i * 2 + 1);
			getActionMap().put(i * 2 + 1, new AbstractAction() {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent arg0) {
					keymap[KEY_KODE] = false;
				}
			});

		}

	}

	public boolean getKeyState(int key) {

		return keymap[key];
	}

}
