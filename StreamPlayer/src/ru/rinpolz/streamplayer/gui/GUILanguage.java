package ru.rinpolz.streamplayer.gui;

import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ru.rinpolz.streamplayer.language.LanguageManager;
import ru.rinpolz.streamplayer.mainlogic.MainClass;

public class GUILanguage extends JDialog {
	private static final long serialVersionUID = 1048014488830896134L;
	private String language;
	
	private JButton english = new JButton("English");
	private JButton russian = new JButton("Русский");
	private JLabel note = new JLabel("Choose language:");
	
	public GUILanguage() {
		this.setTitle("Language Selector");
		this.setSize(200, 100);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		english.setIcon(MainClass.rl.getImage("en"));
		english.setBounds(50, 20, 100, 25);
		english.addActionListener(e -> {
			language = "lang/en_US.lang";
			initLanguage(language);
			MainClass.login = new GUILogin();
			MainClass.login.setVisible(true);
		});
		
		russian.setIcon(MainClass.rl.getImage("ru"));
		russian.setBounds(50, 45, 100, 25);
		russian.setEnabled(false);
		russian.addActionListener(e -> {
			language = "lang/ru_RU.lang";
			initLanguage(language);
			MainClass.login = new GUILogin();
			MainClass.login.setVisible(true);
		});
		
		note.setBounds(55, 1, 100, 20);
		
		this.add(english);
		this.add(russian);
		this.add(note);
		
		this.setVisible(true);
	}
	
	private void initLanguage(String local_name) {
		try {
			MainClass.lang = new LanguageManager(local_name);
			System.out.println("Loading localization file: " + local_name);
			this.dispose();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
