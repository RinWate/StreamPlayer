package ru.rinpolz.streamplayer.equalizer;

import javax.swing.JFrame;

import ru.rinpolz.streamplayer.mainlogic.MainClass;
import ru.rinpolz.streamplayer.mainlogic.Server;
import ru.rinpolz.streamplayer.utill.Utils;

public class ResizerWorker extends Thread {
	static volatile boolean isDone = true;
	boolean dir;
	JFrame frame;

	float hight;

	public ResizerWorker(JFrame f, boolean to) {

		dir = to;
		frame = f;

		if (isDone) {
			isDone = false;
			this.start();
		}

	}

	@Override
	public void run() {

		if (dir) {
			redirectsetvisabtlity(true);
		}

		int dest = 0;

		if (dir) {

			hight = 306;
			dest = 545;
		} else {

			hight = 545;
			dest = 306;

		}

		while (!isDone) {

			if (hight > dest + 1.9d || hight < dest - 1.9d) {
				hight = (float) Utils.lerp(hight, dest, 0.5);
				frame.setSize(400, (int) hight);
				Utils.sleep(22);
			} else {
				frame.setSize(400, (int) dest);
				isDone = true;

			}

		}

		if (!dir) {
			redirectsetvisabtlity(false);

		}

		redirectSet();

	}

	public void redirectSet() {

		if (MainClass.isServer) {
			Server.gui.b_equal.setEnabled(true);
		} else {
			// Client.gui.b_equal.setEnabled(true);
		}

	}

	public void redirectName(String text) {

		if (MainClass.isServer) {
			Server.gui.b_equal.setText(text);
		} else {
			// Client.gui.b_equal.setText(text);
		}

	}

	public void redirectsetvisabtlity(boolean b) {

		if (MainClass.isServer) {
			Server.gui.equalizer.setVisabilyty(b);
		} else {
			// Client.gui.equalizer.setVisabilyty(b);
		}

	}

}
