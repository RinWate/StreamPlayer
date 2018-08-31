package ru.rinpolz.streamplayer.resources;

import java.awt.Image;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class ResourceLoader {

	// Frame's icons
	public final Image login_icon = new ImageIcon(getClass().getResource("login_icon.png")).getImage();
	public final Image icon = new ImageIcon(getClass().getResource("icon.png")).getImage();
	public final Image setting_icon = new ImageIcon(getClass().getResource("settings_icon.png")).getImage();

	private HashMap<String, ImageIcon> ButtonImages = new HashMap<>();

	public ResourceLoader() {
		RegRes("mute.png");
		RegRes("unmute.png");
		RegRes("delete.png");
		RegRes("play.png");
		RegRes("pause.png");
		RegRes("next.png");
		RegRes("replay.png");
		RegRes("replay_e.png");
		RegRes("gear.png");
		RegRes("equ.png");
		RegRes("reload.png");
		RegRes("search.png");
		RegRes("folder.png");
		RegRes("open.png");
		RegRes("goto.png");
	}

	public ImageIcon getImage(String name) {
		return ButtonImages.get(name);
	}

	void RegRes(String filename) {
		try {
			ButtonImages.put(filename.substring(0, filename.length() - 4),
					new ImageIcon(getClass().getResource(filename)));
		} catch (Exception e) {
			System.err.println("Can't load image " + filename);
			e.printStackTrace();
		}

	}

}
