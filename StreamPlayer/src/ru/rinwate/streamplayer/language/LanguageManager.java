package ru.rinwate.streamplayer.language;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class LanguageManager {
	
	HashMap<String, String> parameters = new HashMap<>();
	
	private String line;
	private String[] raw = new String[2];
	
	private String EQUAL = "=";
	private String SPLITTER = "[.]";
	
	private boolean isCorrect = false;
	
	private Scanner scan;
	
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
			parameters.put(raw[0], raw[1].split(EQUAL)[1]);
		}
	}
	
	public String getLocale(String property) {
		return parameters.get(property);
	}
	
	private boolean availableTypes(String text) {
		String[] type = {"button", "frame", "label", "checkbox", "tooltip"};
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
