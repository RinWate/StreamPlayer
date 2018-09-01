package ru.rinpolz.streamplayer.hotkeys;

import java.awt.event.InputEvent;

public class STHotKey {

	String name;
	int id;
	public int index;

	public STHotKey(int id) {

		switch (id) {
		case 1:

			index = 0;
			name = "Alt";
			break;

		case 2:
			index = 1;
			name = "Control";
			break;

		case 4:
			index = 2;
			name = "Shift";
			break;

		}

		this.id = id;
	}

	public int getID() {
		return this.id;
	}

	public static int GetHostKey(int id) {

		switch (id) {
		case 1:

			return InputEvent.ALT_DOWN_MASK;

		case 2:
			return InputEvent.CTRL_DOWN_MASK;

		case 4:
			return InputEvent.SHIFT_DOWN_MASK;

		}

		return InputEvent.SHIFT_DOWN_MASK;

	}

	public String toString() {
		return name;
	}

}
