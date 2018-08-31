package ru.rinpolz.streamplayer.settingsIO;

import java.io.File;
import java.io.Serializable;

public class ConfigObj implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int[] save;
	public int port;
	public int volume;

	// public boolean Shake;

	public boolean oldRmd;
	public String ip;
	public File path;

	public boolean[] chekcboxes;
	public int[] sliders;

	public ConfigObj(int[] array, int p, String i, File pat, boolean oldr, int vol, int[] sliders, boolean[] cbx

	) {

		this.volume = vol;

		this.oldRmd = oldr;
		this.save = array;

		// this.Shake = sha;
		this.ip = i;
		this.path = pat;
		this.port = p;

		this.sliders = sliders;
		this.chekcboxes = cbx;

	}
}
