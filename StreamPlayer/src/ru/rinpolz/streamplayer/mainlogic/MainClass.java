package ru.rinpolz.streamplayer.mainlogic;

import java.io.File;

import ru.rinpolz.streamplayer.gui.GUILanguage;
import ru.rinpolz.streamplayer.gui.GUILogin;
import ru.rinpolz.streamplayer.hotkeys.JKeyType;
import ru.rinpolz.streamplayer.language.LanguageManager;
import ru.rinpolz.streamplayer.resources.ResourceLoader;
import ru.rinpolz.streamplayer.settingsIO.FileLoader;
import ru.rinpolz.streamplayer.utill.TrashGrabber;

public class MainClass {
	public static String ip = "";
	public static int port = 0;
	public static File lastpth;
	public static boolean isRemote;

	public static ResourceLoader rl = new ResourceLoader();
	public static LanguageManager lang;

	static FileLoader loader = new FileLoader();
	public static JKeyType keyListener = new JKeyType();
	static Settings settings = new Settings();
	public static GUILanguage language = new GUILanguage();
	public static GUILogin login;

	public static void main(String[] args) {

		// new GUIServer("DEBUG").showGUI();

		System.setProperty("sun.awt.noerasebackground", "true");
		System.setProperty("sun.java2d.opengl", "true");

		TrashGrabber PooPgrab = new TrashGrabber();
		PooPgrab.start();
		System.out.println(lastpth);

		Settings.setSettings();
		keyListener.initKeys();
	}
	

}
