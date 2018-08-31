package ru.rinpolz.streamplayer.mainlogic;

import ru.rinpolz.streamplayer.util.Utils;

public class VolumeController extends Thread {

	static double prev_volume = 0;
	public static int volume_bar = 0;
	public static double trg_volume = 0;
	public static float current_volume = 0;
	static boolean side;

	public VolumeController(boolean s) {

		side = s;
		this.start();
		System.out.println(volume_bar + "Volume");
	}

	@Override
	public void run() {
		while (true) {

			if (current_volume > trg_volume + 0.1d || current_volume < trg_volume - 0.1d) {
				current_volume = (float) Utils.lerp(current_volume, trg_volume, 0.004);
				Utils.sleep(2);
				updVolme();
			} else {
				Utils.sleep(140);
			}
		}
	}

	public static void updVolme() {
		if (side) {
			if (Server.volume != null) {
				Server.volume.setValue(current_volume);
			}
		} else {
			if (Client.volume != null) {
				Client.volume.setValue(current_volume);
			}
		}
	}

	public static void SetVolume(int d) {
		volume_bar = d;
		if (d == 0) {
			trg_volume = -80;
		} else {
			trg_volume = Utils.map(d, 0, 100, -50f, 0.3f);
		}
	}

	public static void mute() {
		prev_volume = trg_volume;
		trg_volume = -80;
	}

	public static void unmute() {
		current_volume = -80;
		trg_volume = prev_volume;
	}
}
