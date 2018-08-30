package ru.rinpolz.streamplayer.trackControll;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import ru.rinpolz.streamplayer.mainlogic.MainClass;
import ru.rinpolz.streamplayer.mainlogic.Server;
import ru.rinpolz.streamplayer.mainlogic.Settings;
import ru.rinpolz.streamplayer.settingsIO.FileLoader;

public class FileList {

	public static File path;
	public static Comparator<String> sorter = new CompAr();

	public static ConcurrentHashMap<String, Track> filelist = new ConcurrentHashMap<String, Track>();

	public static String[] keys;

	public static boolean isCorrect = false;

	public static JFileChooser fc = new JFileChooser();

	Random r = new Random();

	public JFrame fr = new JFrame("Select Music Folder...");

	public FileList() {

		fr.setSize(500, 500);
		
		fr.setIconImage(new ImageIcon(getClass().getResource("open.png")).getImage());
		
		
		fr.setResizable(false);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setLocationRelativeTo(Server.gui);
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "Mp3/Dir's";
			}

			@Override
			public boolean accept(File f) {
				if (f.isDirectory() || f.getPath().endsWith("mp3")) {
					return true;
				} else {
					return false;
				}
			}
		});

		fr.add(fc);
		fc.setSelectedFile(MainClass.lastpth);
		fc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == JFileChooser.APPROVE_SELECTION) {
					selectFiles(fc.getSelectedFile());
					if (isCorrect) {
						fr.dispose();
						setList();
					}
				} else {
					JOptionPane.showMessageDialog(null, "It must be a directory!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		fr.setVisible(true);

	}

	public Track getSong(boolean select) {

		if (!select) {
			boolean isunic = false;
			Track temp = null;

			while (!isunic) {
				temp = filelist.get(keys[r.nextInt(keys.length)]);

				if (!temp.played || Settings.isRamdOld) {
					isunic = true;
				}

				if (calcplayed() >= filelist.size()) {
					resetAllTracks();
				}
			}
			temp.played = true;
			return temp;
		} else {
			if (Server.gui.list_music.getSelectedValue() != null) {
				return filelist.get(Server.gui.list_music.getSelectedValue());
			} else {
				return filelist.get(keys[0]);
			}

		}

	}

	public static void selectFiles(File file) {
		List<File> names;

		if (file.isDirectory()) {
			Server.gui.b_set.setToolTipText(file.getPath());
			names = Arrays.asList(file.listFiles());

			if (names.size() != 0) {

				for (Entry<String, Track> mapEntry : filelist.entrySet()) {
					if (!names.contains(mapEntry.getValue().file)) {
						filelist.remove(mapEntry.getKey());
					}

				}

				for (File track : names) {

					if (track.getAbsolutePath().endsWith(".mp3")) {
						String tempkey = track.getName().substring(0, track.getName().length() - 4);
						if (!filelist.containsKey(tempkey)) {
							filelist.put(tempkey, new Track(track.getName(), track));
							
						}
					}
				}

				if (filelist.size() == 0) {
					JOptionPane.showMessageDialog(null, "I can't find any music files...\nTry to another one!",
							"Oops...", JOptionPane.ERROR_MESSAGE);
					isCorrect = false;

				} else {
					isCorrect = true;
				}

				setList();

				if (filelist.size() != 0) {

					if (MainClass.lastpth != null) {
						if (!MainClass.lastpth.equals(fc.getSelectedFile())) {
							Server.isSkip = true;
						}
					}

					MainClass.lastpth = fc.getSelectedFile();
					FileLoader.saveSettings();
				}

				Server.gui.setEnabled(true);
			} else {
				JOptionPane.showMessageDialog(null, "Empty directory", "Error", JOptionPane.ERROR_MESSAGE);
			}

		} else {
			JOptionPane.showMessageDialog(null, "Invalid directory", "Error", JOptionPane.ERROR_MESSAGE);
		}

		// ¬ключение кнопок после выбора директории...
		Server.gui.b_set.setEnabled(true);

	}

	public int calcplayed() {
		int pl = 0;
		for (int i = 0; i < keys.length; i++) {
			if (filelist.get(keys[i]).played) {
				pl++;
			}
		}
		Server.gui.l_trackcount.setToolTipText("Played " + pl);
		return pl;
	}

	public void resetAllTracks() {
		for (int i = 0; i < keys.length; i++) {
			filelist.get(keys[i]).played = false;
		}
	}

	public static void sort(String text) {
		if (text.length() == 0) {
			Arrays.sort(keys);
		} else {
			CompAr.text = text;
			Arrays.sort(keys);
			Arrays.sort(keys, sorter);
		}
		Server.gui.list_music.setListData(keys);
	}

	public static void setList() {
		int index = 0;
		keys = new String[filelist.size()];
		for (Entry<String, Track> mapEntry : filelist.entrySet()) {
			keys[index] = mapEntry.getKey();
			index++;
		}

		Arrays.sort(keys);
		Server.gui.list_music.setListData(keys);
		Server.gui.l_trackcount.setText(keys.length + " files");
	}
}