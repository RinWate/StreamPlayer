package ru.rinpolz.streamplayer.settingsIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.UIManager;

import ru.rinpolz.streamplayer.equalizer.Equalizer;
import ru.rinpolz.streamplayer.mainlogic.MainClass;
import ru.rinpolz.streamplayer.mainlogic.Settings;
import ru.rinpolz.streamplayer.mainlogic.VolumeController;
import ru.rinpolz.streamplayer.utill.Utils;

public class FileLoader {

	static String homeDir = System.getProperty("user.home");
	public static ConfigObj CurrentSettings;

	public FileLoader() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// SwingUtilities.updateComponentTreeUI(mainframe);

		loadSettings();

	}

	public static void saveDefault() {
		System.out.println("Setting to default");
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		File configF = new File(homeDir + "\\.RadioSettings\\Settings..");

		if (!configF.exists()) {
			try {
				configF.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			System.out.println("Rewriting file...");
			fos = new FileOutputStream(configF);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(new ConfigObj(new int[] { 1, 39, 1, 37, 1, 38, 1, 77, 1, 40, 1, 34 }, MainClass.port,
					MainClass.ip, new File(""), false, 50, Utils.GetfilledArrey(20, 32), new boolean[32]));

			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadSettings() {

		boolean done = false;
		FileInputStream fos;

		File dir = new File(homeDir + "\\.RadioSettings");
		dir.mkdir();
		File configF = new File(homeDir + "\\.RadioSettings\\Settings..");
		CurrentSettings = null;
		int failcont = 0;
		do {
			try {

				fos = new FileInputStream(configF);
				ObjectInputStream ois = new ObjectInputStream(fos);

				// Wtf
				CurrentSettings = (ConfigObj) ois.readObject();

				ois.close();
				done = true;
				MainClass.ip = CurrentSettings.ip;
				MainClass.port = CurrentSettings.port;

				MainClass.lastpth = CurrentSettings.path;

				Settings.keys = CurrentSettings.save;

				MainClass.tempHolder = CurrentSettings.sliders;
				MainClass.tempBoolholder = CurrentSettings.chekcboxes;

				Settings.isRamdOld = CurrentSettings.oldRmd;

				VolumeController.volume_bar = CurrentSettings.volume;

			} catch (Exception e) {
				failcont++;
				fos = null;
				saveDefault();
				e.printStackTrace();
			}

			if (failcont >= 10) {
				System.out.println("Error while loading settings!");
				System.exit(0);
			}

		} while (!done);

	}

	public static void saveSettings() {

		// 11
		ConfigObj seving;
		if (MainClass.isServer) {
			seving = new ConfigObj(Settings.keys, MainClass.port, MainClass.ip, MainClass.lastpth, Settings.isRamdOld,
					VolumeController.volume_bar, Equalizer.GetSliderPos(), Equalizer.GetCheckboxes());

		} else {
			seving = new ConfigObj(Settings.keys, MainClass.port, MainClass.ip, MainClass.lastpth, Settings.isRamdOld,
					VolumeController.volume_bar, MainClass.tempHolder, MainClass.tempBoolholder);

		}

		ObjectOutputStream oos;
		FileOutputStream fos;
		File file = new File(homeDir + "/.RadioSettings/Settings..");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Error while saving settings!");
				System.exit(0);
			}
		}

		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(seving);
			oos.close();
		} catch (IOException e) {
			System.out.println("Can't save");
			e.printStackTrace();
		}
	}
}