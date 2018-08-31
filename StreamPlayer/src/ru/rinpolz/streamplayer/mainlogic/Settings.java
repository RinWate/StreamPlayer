package ru.rinpolz.streamplayer.mainlogic;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ru.rinpolz.streamplayer.hotkeys.HotKey;
import ru.rinpolz.streamplayer.hotkeys.KeyChooser;
import ru.rinpolz.streamplayer.settingsIO.FileLoader;
import ru.rinpolz.streamplayer.utill.Utils;

public class Settings {
	public static boolean isOpened;

	public static int[] keys = { 1, 39 // Skip 0

			, 1, 37 // Pause 2
			, 1, 38 // Volume-Down 4
			, 1, 77 // Mute ??????7777
			, 1, 40 // Volume-Up 8
			, 1, 34 };

	// public static boolean isShaking = false;

	public static boolean isRamdOld = false;
	public static boolean isDebug = false;
	public static boolean isShowFormat = false;
	public static boolean isAllowClientSkip = false;

	public static JFrame mainframe = new JFrame();
	public static JButton set_skip = new JButton("Set");
	public static JButton set_pause = new JButton("Set");
	public static JButton set_mute = new JButton("Set");
	public static JButton set_volume_up = new JButton("Set");
	public static JButton set_volume_down = new JButton("Set");

	HotKey[] hotkeys = { new HotKey(1), new HotKey(2), new HotKey(4), new HotKey(8) };

	public static JComboBox<HotKey> box_skip = new JComboBox<>();
	public static JComboBox<HotKey> box_pause = new JComboBox<>();
	public static JComboBox<HotKey> box_mute = new JComboBox<>();
	public static JComboBox<HotKey> box_volume_up = new JComboBox<>();
	public static JComboBox<HotKey> box_volume_down = new JComboBox<>();

	public static JButton set_replay = new JButton("Set");
	public static JComboBox<HotKey> box_replay = new JComboBox<>();

	private JButton save = new JButton("Save");
	private JButton set_default = new JButton("Default");

	private JLabel l_pause = new JLabel("Pause Key:");
	private JLabel l_mute = new JLabel("Mute Key:");
	private JLabel l_skip = new JLabel("Skip Key:");
	private JLabel l_volume_up = new JLabel("Volume Up Key:");
	private JLabel l_volume_down = new JLabel("Volume Down Key:");
	private JLabel l_version = new JLabel("v" + Utils.VERSION);

	private JLabel l_replay = new JLabel("Replay Key:");

	// public static JCheckBox cb_shaking = new JCheckBox("Shaking");
	public static JCheckBox cb_oldRamdom = new JCheckBox("OldRmd");
	public static JCheckBox cb_fileformat = new JCheckBox("File Formats");
	public static JCheckBox cb_allowskip = new JCheckBox("Radio Mod");

	public Settings() {

		for (HotKey hotKey : hotkeys) {
			box_skip.addItem(hotKey);
			box_mute.addItem(hotKey);
			box_pause.addItem(hotKey);
			box_volume_up.addItem(hotKey);
			box_volume_down.addItem(hotKey);
			box_replay.addItem(hotKey);

		}

		mainframe.setSize(190, 390);
		mainframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainframe.setLocationRelativeTo(null);
		mainframe.setLayout(null);
		mainframe.setResizable(false);
		mainframe.setIconImage(MainClass.rl.setting_icon);

		mainframe.add(save);
		mainframe.add(set_default);
		mainframe.add(set_skip);
		mainframe.add(set_pause);
		mainframe.add(set_mute);
		mainframe.add(set_volume_up);
		mainframe.add(set_volume_down);

		mainframe.add(l_mute);
		mainframe.add(l_pause);
		mainframe.add(l_skip);
		mainframe.add(l_volume_up);
		mainframe.add(l_volume_down);
		mainframe.add(l_version);

		mainframe.add(box_mute);
		mainframe.add(box_skip);
		mainframe.add(box_pause);
		mainframe.add(box_volume_up);
		mainframe.add(box_volume_down);

		mainframe.add(l_replay);
		mainframe.add(box_replay);
		mainframe.add(set_replay);

		// mainframe.add(cb_shaking);
		mainframe.add(cb_oldRamdom);

		mainframe.add(cb_fileformat);
		mainframe.add(cb_allowskip);

		l_version.setBounds(133, -3, 50, 20);
		l_version.setHorizontalAlignment(JLabel.RIGHT);

		l_skip.setBounds(10, 5, 100, 20);
		box_skip.setBounds(10, 25, 75, 20);

		set_skip.setBounds(100, 25, 75, 20);

		l_pause.setBounds(10, 50, 100, 20);
		box_pause.setBounds(10, 70, 75, 20);

		set_pause.setBounds(100, 70, 75, 20);

		l_mute.setBounds(10, 95, 100, 20);
		box_mute.setBounds(10, 115, 75, 20);

		set_mute.setBounds(100, 115, 75, 20);

		l_volume_up.setBounds(10, 140, 100, 20);
		box_volume_up.setBounds(10, 160, 75, 20);
		set_volume_up.setBounds(100, 160, 75, 20);

		l_volume_down.setBounds(10, 185, 100, 20);
		box_volume_down.setBounds(10, 205, 75, 20);
		set_volume_down.setBounds(100, 205, 75, 20);

		l_replay.setBounds(10, 230, 100, 20);
		box_replay.setBounds(10, 250, 75, 20);
		set_replay.setBounds(100, 250, 75, 20);

		// cb_shaking.setBounds(10, 275, 100, 20);
		cb_oldRamdom.setBounds(110, 275, 100, 20);

		cb_fileformat.setBounds(10, 295, 125, 20);
		cb_allowskip.setBounds(10, 315, 125, 20);

		cb_fileformat.setToolTipText("Not working");
		cb_allowskip.setToolTipText("Allows clients to skip current track");

		cb_allowskip.setEnabled(false);

		cb_fileformat.setEnabled(false);

		save.setBounds(10, 335, 75, 20);
		set_default.setBounds(100, 335, 75, 20);

		mainframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Server.gui.b_settings.setEnabled(true);
				Client.gui.b_settings.setEnabled(true);
				Client.gui.setEnabled(true);
				Server.gui.setEnabled(true);
			}
		});

		box_skip.addActionListener(e -> {
			HotKey g = (HotKey) box_skip.getSelectedItem();
			keys[0] = g.getID();
		});

		box_pause.addActionListener(e -> {
			HotKey g = (HotKey) box_pause.getSelectedItem();
			keys[2] = g.getID();
		});

		box_volume_up.addActionListener(e -> {
			HotKey g = (HotKey) box_volume_up.getSelectedItem();
			keys[4] = g.getID();
		});

		box_mute.addActionListener(e -> {
			HotKey g = (HotKey) box_mute.getSelectedItem();
			keys[6] = g.getID();
		});

		box_volume_down.addActionListener(e -> {

			HotKey g = (HotKey) box_volume_down.getSelectedItem();
			keys[8] = g.getID();
		});

		box_replay.addActionListener(e -> {
			HotKey g = (HotKey) box_replay.getSelectedItem();
			keys[10] = g.getID();

		});

		set_replay.addActionListener(e -> {
			new KeyChooser(11);
			mainframe.setEnabled(false);
		});

		set_skip.addActionListener(e -> {
			new KeyChooser(1);
			mainframe.setEnabled(false);
		});

		set_pause.addActionListener(e -> {
			new KeyChooser(3);
			mainframe.setEnabled(false);
		});

		set_volume_up.addActionListener(e -> {
			new KeyChooser(5);
			mainframe.setEnabled(false);
		});

		set_mute.addActionListener(e -> {
			new KeyChooser(7);
			mainframe.setEnabled(false);
		});

		set_volume_down.addActionListener(e -> {
			new KeyChooser(9);
			mainframe.setEnabled(false);
		});

		// cb_shaking.addActionListener(e -> {
		// isShaking = cb_shaking.isSelected();
		// });

		cb_oldRamdom.addActionListener(e -> {
			isRamdOld = cb_oldRamdom.isSelected();
		});

		cb_fileformat.addActionListener(e -> {
			isShowFormat = cb_fileformat.isSelected();
		});

		cb_allowskip.addActionListener(e -> {

		});

		set_default.addActionListener(e -> {
			setDefault();
			FileLoader.saveDefault();
			JOptionPane.showMessageDialog(mainframe, "Reset to Default!", "Default", JOptionPane.INFORMATION_MESSAGE);
			mainframe.dispose();
			setFocus(true);
		});

		save.addActionListener(e -> {
			FileLoader.saveSettings();
			MainClass.keyListener.initKeys();
			JOptionPane.showMessageDialog(mainframe, "Successfully saved", "Saving", JOptionPane.INFORMATION_MESSAGE);
		});
	}

	public static void setDefault() {
		keys = new int[] {

				1, 39 // Skip 0
				, 1, 37 // Pause 2
				, 1, 38 // Volume-Down 4
				, 1, 77 // Mute 6
				, 1, 40// Volume-Up 8

				, 1, 34 };

		isRamdOld = false;
		// isShaking = false;
		isAllowClientSkip = true;
		isDebug = false;
		isShowFormat = false;

		FileLoader.saveDefault();
		MainClass.keyListener.initKeys();
		setSettings();
	}

	public static void setFocus(boolean state) {
		Server.gui.setEnabled(state);
		Server.gui.b_settings.setEnabled(true);
		if (state) {
			Server.gui.setAlwaysOnTop(true);
			Server.gui.setAlwaysOnTop(false);
		}
	}

	public static void setSettings() {

		box_skip.setSelectedIndex(cheks(keys[0]));
		SetButtonName(set_skip, KeyEvent.getKeyText(keys[1]));

		box_pause.setSelectedIndex(cheks(keys[2]));
		SetButtonName(set_pause, KeyEvent.getKeyText(keys[3]));

		box_mute.setSelectedIndex(cheks(keys[6]));
		SetButtonName(set_mute, KeyEvent.getKeyText(keys[7]));

		box_volume_up.setSelectedIndex(cheks(keys[4]));
		SetButtonName(set_volume_up, KeyEvent.getKeyText(keys[5]));

		box_volume_down.setSelectedIndex(cheks(keys[8]));
		SetButtonName(set_volume_down, KeyEvent.getKeyText(keys[9]));

		box_replay.setSelectedIndex(cheks(keys[10]));
		SetButtonName(set_replay, KeyEvent.getKeyText(keys[11]));

		// cb_shaking.setSelected(isShaking);
		cb_oldRamdom.setSelected(isRamdOld);

	}

	public static int cheks(int ind) {
		int out = 0;
		switch (ind) {
		case 2:
			out = 1;
			break;
		case 4:
			out = 2;
			break;
		case 8:
			out = 3;
			break;
		}
		return out;
	}

	public static void SetButtonName(JButton b, String n) {
		b.setText(n);
		b.setToolTipText(n);
	}
}
