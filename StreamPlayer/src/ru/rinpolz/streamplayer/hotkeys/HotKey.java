package ru.rinpolz.streamplayer.hotkeys;

public class HotKey {

	String name;
	int id;
	public	int index;

	public HotKey(int id) {

		switch (id) {
		case 1:

			index =0;
			name = "Alt";
			break;

		case 2:
			index =1;
			name = "Control";
			break;

		case 4:
			index =2;
			name = "Shift";
			break;

		case 8:
			index =3;
			name = "Win";

			break;

		}

		
		this.id = id;
	}

	public int getID() {
		return this.id;
	}

	public String toString() {
		return name;
	}

}
