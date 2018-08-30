package ru.rinpolz.streamplayer.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ru.rinpolz.streamplayer.equalizer.Equalizer;
import ru.rinpolz.streamplayer.equalizer.ResizerWorker;
import ru.rinpolz.streamplayer.listeners.SKeyListener;
import ru.rinpolz.streamplayer.listeners.SMouseListener;
import ru.rinpolz.streamplayer.listeners.SMouseMotionListener;
import ru.rinpolz.streamplayer.mainlogic.MainClass;
import ru.rinpolz.streamplayer.mainlogic.Server;
import ru.rinpolz.streamplayer.mainlogic.Settings;
import ru.rinpolz.streamplayer.mainlogic.VolumeController;
import ru.rinpolz.streamplayer.trackControll.FileList;
import ru.rinpolz.streamplayer.utill.Utils;

public class GUIServer extends JFrame {

	public boolean hasReplay = false;

	public static String Search_text = "";

	Desktop desktop = null;

	private static final long serialVersionUID = 6604939069695118255L;

	boolean isShow = false;

	public SlideLinePanel sl_currentSong = new SlideLinePanel();
	public JList<String> list_music = new JList<String>();
	public JScrollPane sp_list = new JScrollPane(list_music);

	public JButton b_play = new JButton(MainClass.rl.getImage("pause"));
	public JButton b_next = new JButton(MainClass.rl.getImage("next"));
	public JButton b_mute = new JButton(MainClass.rl.getImage("unmute"));

	public JSlider s_volume = new JSlider();

	public JButton b_replay = new JButton();
	public JButton b_delete = new JButton(MainClass.rl.getImage("delete"));
	public JButton b_settings = new JButton(MainClass.rl.getImage("gear"));
	public JButton b_openfolder = new JButton(MainClass.rl.getImage("open"));
	public JButton b_set = new JButton(MainClass.rl.getImage("folder"));
	public JButton b_reload = new JButton(MainClass.rl.getImage("reload"));

	public JButton b_goto = new JButton(MainClass.rl.getImage("goto"));

	public JLabel l_online = new JLabel("Online: ");
	public JLabel l_status = new JLabel("Status: ");
	public JLabel l_trackcount = new JLabel(">?<");

	public JLabel l_timer = new JLabel("00:00|00:00", SwingConstants.CENTER);

	public JButton b_equal = new JButton(MainClass.rl.getImage("equ"));

	public JButton b_metadata = new JButton("Metadata");
	public Equalizer equalizer = new Equalizer(this, 300);
	public JTextField tf_search = new JTextField();
	public JLabel l_timing = new JLabel("--:--");

	public GUIServer(String name) {
		System.out.println("Init Server GUI...");
		this.setIconImage(MainClass.rl.icon);
		this.getContentPane().setBackground(Color.lightGray);
		this.setTitle(name);
		this.setSize(400, 305);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setResizable(false);

		/////////////////// Labels,chb//////////////////

		this.add(sl_currentSong);
		sl_currentSong.setBounds(1, 14, 392, 20);

		this.add(l_status);
		l_status.setBounds(192, -3, 200, 20);
		l_status.setFont(Utils.STANDART_FONT);
		l_status.setHorizontalAlignment(JLabel.RIGHT);

		this.add(l_timing);
		l_timing.setBounds(323, 60, 50, 25);
		l_timing.setFont(Utils.TIMER_FONT);

		this.add(l_timer);
		l_timer.setBounds(60, 253, 105, 20);
		l_timer.setFont(Utils.TIMER_FONT);

		this.add(l_trackcount);
		l_trackcount.setBounds(185, -3, 50, 20);
		l_trackcount.setFont(new Font("Arial", Font.BOLD, 12));

		this.add(l_online);
		l_online.setBounds(3, -3, 150, 20);
		l_online.setFont(Utils.STANDART_FONT);

		/////////////////////////////////////////

		/// //////////////////////////////////// List music
		list_music.setLayoutOrientation(JList.VERTICAL);
		list_music.setFont(Utils.STANDART_FONT);
		list_music.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.add(sp_list);
		sp_list.setBounds(2, 61, 286, 187);
		// >
		list_music.addListSelectionListener(e -> {
			updateDeleteButtonState();
		});

		list_music.addMouseListener(new SMouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() >= 2 && arg0.getButton() == 1) {
					sl_currentSong.resetAll(false);
					Server.isSet = true;
					Server.isSkip = true;

				}
			}
		});
		//// >
		list_music.addKeyListener(new SKeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {

				if (arg0.getKeyCode() == 10) {
					sl_currentSong.resetAll(false);
					Server.isSet = true;
					Server.isSkip = true;
				} else if (arg0.getKeyCode() == 38 && list_music.getSelectedIndex() == 0) {
					tf_search.requestFocus();
					list_music.clearSelection();
					b_delete.setEnabled(false);
				}
			}
		});

		sl_currentSong.addMouseMotionListener(new SMouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (e.isShiftDown()) {
					sl_currentSong.setPresset(e.getX());
					l_timing.setText(Server.GetPresetPoss(e.getX()));
				} else {
					l_timing.setText("--:--");
					sl_currentSong.clearPreset();
				}
				Utils.sleep(33);
			}
		});
		//// >
		sl_currentSong.addMouseListener(new SMouseListener() {
			
			@Override
			public void mouseEntered(MouseEvent e) {
				sl_currentSong.reposPresset();
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isShiftDown() && sl_currentSong.isEnabled()) {
					Server.SetTrackPos(e.getX());
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				l_timing.setText("--:--");
				sl_currentSong.clearPreset();
			}
		});

		// Delete button///////////////////////////////////////////////////////////////
		this.add(b_delete);
		b_delete.setBounds(301, 35, 30, 25);
		b_delete.setEnabled(false);

		// >
		b_delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!list_music.isSelectionEmpty()) {
					File t = FileList.filelist.get(list_music.getSelectedValue()).file;
					File ren = new File(t.getAbsolutePath() + ".dis");
					if (!FileList.filelist.get(list_music.getSelectedValue()).file.getName()
							.equals(Server.CurrentTrack.getName())) {
						if (t.renameTo(ren)) {
							FileList.filelist.remove(t.getName().substring(0, t.getName().length() - 4));
							FileList.sort(Search_text);
							FileList.setList();
							b_delete.setEnabled(false);
						}
					}
				}
			}
		});

		// Settings button//////////////////////////////////////////////////////////
		this.add(b_settings);
		b_settings.setBounds(225, 250, 30, 25);

		b_settings.addActionListener(e -> {
			Settings.mainframe.setLocationRelativeTo(this);
			Settings.mainframe.setVisible(true);
			setEnabled(false);
			b_settings.setEnabled(false);
		});

		// OpenFolder Button
		this.add(b_openfolder);
		b_openfolder.setBounds(332, 35, 30, 25);
		b_openfolder.setToolTipText("Open current folder");
		b_openfolder.addActionListener(e -> {
			if (Desktop.isDesktopSupported()) {
				desktop = Desktop.getDesktop();

				try {

					// Windows
					// Runtime.getRuntime().exec("explorer.exe /select," +
					// Server.CurrentTrack.getAbsolutePath());
					// Mac
					// Runtime.getRuntime().exec("open -R filename" +
					// Server.CurrentTrack.getAbsolutePath());

					desktop.open(MainClass.lastpth);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		// Slider Volume/////////////////////////////
		this.add(s_volume);
		s_volume.setMaximum(90);
		s_volume.setBounds(288, 250, 105, 25);
		s_volume.setBackground(Color.lightGray);
		// >
		s_volume.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				VolumeController.SetVolume(s_volume.getValue());
			}
		});
		//// >
		s_volume.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (s_volume.isEnabled()) {
					byte how = 5;
					if (e.isShiftDown()) {
						how = 1;
					}
					if (e.getWheelRotation() == -1) {
						s_volume.setValue(s_volume.getValue() + how);
					} else {
						s_volume.setValue(s_volume.getValue() - how);
					}
				}
			}
		});

		// Search Field
		this.add(tf_search);
		tf_search.setBounds(2, 35, 236, 25);
		tf_search.setFont(Utils.STANDART_FONT);
		tf_search.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent arg0) {
				Search_text = tf_search.getText().toLowerCase();
				list_music.clearSelection();
				sp_list.getVerticalScrollBar().setValue(0);
				sp_list.getHorizontalScrollBar().setValue(0);
				b_delete.setEnabled(false);
				FileList.sort(Search_text);
			}
		});

		//// >
		tf_search.addKeyListener(new SKeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (tf_search.getText().length() >= 255) {
					e.consume();
					tf_search.setText(tf_search.getText().substring(0, 255));
					Utils.BeePBoop();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10 || e.getKeyCode() == 40) {
					list_music.requestFocus();
					list_music.setSelectedIndex(0);
				}
			}
		});

		/////////////////////// Single //////////////////////

		// Reload Button/////
		this.add(b_reload);
		b_reload.setBounds(194, 250, 30, 25);
		b_reload.setMultiClickThreshhold(700);
		// >
		b_reload.addActionListener(e -> {
			FileList.selectFiles(FileList.fc.getSelectedFile());
			FileList.sort(Search_text);
		});

		// Play Button
		this.add(b_play);
		b_play.setBounds(2, 250, 30, 25);
		// >
		b_play.addActionListener(e -> {
			Server.volume.setValue(-80f);
			Server.pause();
		});

		// Replay Button
		this.add(b_replay);
		b_replay.setIcon(MainClass.rl.getImage("replay"));
		b_replay.setBounds(239, 35, 30, 25);

		b_replay.addActionListener(e -> {
			if (hasReplay) {
				b_replay.setIcon(MainClass.rl.getImage("replay"));
				hasReplay = false;
			} else {
				b_replay.setIcon(MainClass.rl.getImage("replay_e"));
				hasReplay = true;
			}

		});

		// Metadata Button
		this.add(b_metadata);
		b_metadata.setBounds(290, 215, 100, 20);
		b_metadata.addActionListener(e -> {
			new Metadata();
		});

		// SetDit Button
		this.add(b_set);
		b_set.setBounds(163, 250, 30, 25);
		// >
		b_set.addActionListener(e -> {
			Server.playlist.fr.setLocationRelativeTo(this);
			Server.playlist.fr.setVisible(true);
			setEnabled(false);
		});

		// Next button
		this.add(b_next);
		b_next.setBounds(31, 250, 30, 25);
		b_next.addActionListener(e -> {
			sl_currentSong.resetAll(false);
			Server.isSkip = true;
		});

		// Mute button
		this.add(b_mute);

		b_mute.setBounds(256, 250, 30, 25);
		// >
		b_mute.addActionListener(e -> {
			Server.mute();
		});

		// Equalizer button
		this.add(b_equal);
		b_equal.setBounds(363, 35, 30, 25);
		equalizer.initEquo();
		// >
		b_equal.addActionListener(e -> {
			b_equal.setEnabled(false);
			if (isShow) {
				isShow = false;
			} else {
				isShow = true;
			}
			new ResizerWorker(this, isShow);
		});

		// GoTO button
		this.add(b_goto);
		b_goto.setBounds(270, 35, 30, 25);
		b_goto.addActionListener(e -> {
			// TODO SOMETEST
			list_music.setSelectedValue(
					Server.CurrentTrack.getName().substring(0, Server.CurrentTrack.getName().length() - 4), true);

		});

	}

	public void showGUI() {
		this.setVisible(true);
		try {
			// 4
			this.createBufferStrategy(2);
		} catch (Exception e) {
			System.err.println("≈банные буферы");
			e.printStackTrace();
		}
	}

	public void updateDeleteButtonState() {

		if (!list_music.isSelectionEmpty()) {
			if (FileList.filelist.get(list_music.getSelectedValue()).file.getName()
					.equals(Server.CurrentTrack.getName())) {

				b_delete.setEnabled(false);
			} else {
				b_delete.setEnabled(true);
			}
		}
	}

	public void ShakeOff() {
		if (!Server.isPaused && !Server.isSkip && Settings.isShaking) {
			Point last = this.getLocation();
			this.setLocation(last.x + Utils.getRandom(2), last.y + Utils.getRandom(2));
			Utils.sleep(20);
			this.setLocation(last);
		}
	}
}
