package ru.rinpolz.streamplayer.utill;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.util.Arrays;
import java.util.Random;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.tritonus.share.sampled.file.TAudioFileFormat;

import ru.rinpolz.streamplayer.mainlogic.MainClass;

public abstract class Utils {

	public static Random random = new Random();
	/**
	 *  A someshit finals strings
	 */
	public static final String PLAY = MainClass.lang.getLocale("status::play");
	public static final String PAUSE = MainClass.lang.getLocale("status::pause");
	public static final String SYNC = MainClass.lang.getLocale("status:sync");
	public static final String STATUS = MainClass.lang.getLocale("status::name");
	public static final String ONLINE = MainClass.lang.getLocale("online::name");
	public static final String VERSION = "1.9 PR";

	public static final Font STANDART_FONT = new Font("Arial", Font.BOLD, 11);
	public static final Font FILE_LIST_FONT = new Font("LucidaSans", Font.BOLD, 10);
	public static final Font TIMER_FONT = new Font("Consolas", Font.BOLD, 12);
	public static final Font CHECK_BOX_FONT = new Font("Consolas", Font.BOLD, 8);
	public static final Font LABEL_FONT = new Font("Arial", 2, 11);
	public static final Font SLIDER_FONT = new Font("LucidaSans", Font.BOLD, 15);

	public static final Color PROGRESS_COLOR = new Color(245, 155, 5, 130);
	public static final Color DEST_COLOR = new Color(255, 5, 0, 200);

	public static final Color PRESET_COLOR = new Color(66, 192, 166, 150);

	public static double map(double n, double start1, double stop1, double start2, double stop2) {
		return ((n - start1) / (stop1 - start1)) * (stop2 - start2) + start2;
	}

	public static double lerp(double point1, double point2, double alpha) {
		return point1 + alpha * (point2 - point1);
	}

	public static void sleep(int tims) {
		try {
			Thread.sleep(tims);
		} catch (InterruptedException e) {
		}
	}

	public static String getDuration(File file) {
		String ret = "?";
		try {
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
			long microseconds = (long) ((TAudioFileFormat) fileFormat).properties().get("duration");
			ret = getTime(microseconds);
		} catch (Exception e) {
			e.printStackTrace();
			return ret;
		}
		return ret;
	}
	
	public static String getWord(int num_of_listeners) {
		if (num_of_listeners == 1) {
			return MainClass.lang.getLocale("listeners::one");
		} else {
			return MainClass.lang.getLocale("listeners::many");
		}
	}

	
	@SuppressWarnings("unused")
	private byte[] adjustVolume(byte[] audioSamples, float volume) {
		// Big indian
		byte[] array = new byte[audioSamples.length];
		for (int i = 0; i < array.length; i += 2) {
			// convert byte pair to int
			short buf1 = audioSamples[i + 1];
			short buf2 = audioSamples[i];
			buf1 = (short) ((buf1 & 0xff) << 8);
			buf2 = (short) (buf2 & 0xff);
			short res = (short) (buf1 | buf2);
			res = (short) (res * volume);
			// convert back
			array[i] = (byte) res;
			array[i + 1] = (byte) (res >> 8);

		}
		return array;
	}

	public static long getMs(File file) {
		try {
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
			return (long) ((TAudioFileFormat) fileFormat).properties().get("duration");
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static String getTime(double ms) {

		String formatM = "";
		String formatS = "";

		double mili = (ms / 1000);
		double sec = (mili / 1000) % 60;
		double min = (mili / 1000) / 60;

		if (sec < 10) {
			formatS = "0" + (int) sec;
		} else {
			formatS = (int) sec + "";
		}
		if (min < 10) {
			formatM = "0" + (int) min;
		} else {
			formatM = (int) min + "";
		}
		return formatM + ":" + formatS;
	}

	public static final void BeePBoop() {
		Toolkit.getDefaultToolkit().beep();
	}

	public static final int getRandom(int dest) {
		return random.nextInt(dest) - random.nextInt(dest);
	}

	public static final double getDurationOfWavInSeconds(File file) {
		AudioInputStream stream = null;
		try {
			stream = AudioSystem.getAudioInputStream(file);
			AudioFormat format = stream.getFormat();
			return (file.length() / format.getSampleRate() / (format.getSampleSizeInBits() / 8.0)
					/ format.getChannels());
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static final int[] GetfilledArrey(int how, int leg) {
		int[] ar = new int[leg];
		Arrays.fill(ar, how);
		return ar;
	}
}
