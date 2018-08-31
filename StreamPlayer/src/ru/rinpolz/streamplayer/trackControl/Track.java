package ru.rinpolz.streamplayer.trackControl;
import java.io.File;

public class Track {
	
	public String name;
	public File file;
	public boolean played = false;
	
	public Track(String name, File file) {
		this.name = name;
		this.file = file;
	}
	
	public String toString() {
		return name;
	}
}
