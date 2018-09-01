package ru.rinpolz.streamplayer.network;

public class StatUsParser {

	String[] strings;

	public void parse(String s) {
		strings = null;
		try {
			strings = s.split("\n");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String get(int i) {
		try {
			return strings[i];
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	public String getTitle() {
		return get(0);
	}

	public String getStatus() {
		return get(1);
	}

	public String getTimeline() {
		return get(2);
	}

	public String getVersion() {
		return get(3);
	}

}
