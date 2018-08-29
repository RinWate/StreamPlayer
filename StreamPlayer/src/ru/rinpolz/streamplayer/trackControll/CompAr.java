package ru.rinpolz.streamplayer.trackControll;

import java.util.Comparator;

public class CompAr implements Comparator<String> {

	public static String text = "";

	public int compare(String o1, String o2) {
		int store1 = 0;
		int store2 = 0;

		if (o1.toLowerCase().contains(text)) {
			store1 = 1;
		}

		if (o2.toLowerCase().contains(text)) {
			store2 = 1;
		}

		if (store1 > store2) {
			return -1;
		}

		if (store1 == store2) {
			return 0;
		}
		return 1;

	}

}
