package ru.rinpolz.streamplayer.language;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class LanguageManager {
	
	HashMap<String, String> parameters = new HashMap<>();
	
	private String line;
	private String[] raw = new String[4];
	private String EQUAL = "=";
	private String SPLITTER = "[.]";
	private String SPLIT = "::";
	
	private boolean isCorrect = false;
	
	private Scanner scan;
	private String language;
	
	public LanguageManager(String languageSet) throws FileNotFoundException {
		this.language = languageSet;
		File file = new File(languageSet);
		loadFile(file);
	}
	
	public void loadFile(File lang_file) throws FileNotFoundException {
		scan = new Scanner(lang_file);
		
		while (scan.hasNextLine()) {
			line = scan.nextLine(); 
			if (line.contains(".") && line.contains("=") && availableTypes(line)) {
				isCorrect = true;
			} else {
				isCorrect = false;
				System.err.println("Language Compilation Error!");
				parameters.clear();
				break;
			}
			System.out.println(line);
			raw = line.split(SPLITTER);
			System.out.println(raw[1].split(EQUAL)[0]);
			parameters.put(raw[1].split(EQUAL)[0], raw[1].split(EQUAL)[1]);
		}
	}
	
	public String getLocale(String property) {
		return parameters.get(property);
	}
	
	private boolean availableTypes(String text) {
		String[] type = {"button", "frame", "label", "checkbox"};
		boolean isContains = false;
		for (String s : type) {
			if (text.contains(s)) {
				isContains = true;
				break;
			}
		}
		return isContains;
	}
}
