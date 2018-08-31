package ru.rinpolz.streamplayer.util;

public class TrashGrabber extends Thread {

	public void run() {
		this.setName("PoooP-Grab");
		this.start();
		while (true) {
			try {
				Thread.sleep(10000);
				System.gc();
			} catch (InterruptedException e) {
			}

		}

	}
}