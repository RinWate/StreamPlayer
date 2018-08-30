package ru.rinpolz.streamplayer.utill;

public class TrashGrabber extends Thread {

	public void run() {
		this.setPriority(MIN_PRIORITY);
		this.setName("PoooP-Grab");
		while (true) {
			try {
				Thread.sleep(10000);
				System.gc();
			} catch (InterruptedException e) {
			}

		}

	}
}