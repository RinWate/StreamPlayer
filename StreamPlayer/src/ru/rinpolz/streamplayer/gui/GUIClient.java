package ru.rinpolz.streamplayer.gui;

import java.awt.Color;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ru.rinpolz.streamplayer.mainlogic.Client;
import ru.rinpolz.streamplayer.mainlogic.MainClass;
import ru.rinpolz.streamplayer.mainlogic.Settings;
import ru.rinpolz.streamplayer.mainlogic.VolumeController;
import ru.rinpolz.streamplayer.network.ClientInputReader;
import ru.rinpolz.streamplayer.network.NetCodes;
import ru.rinpolz.streamplayer.utill.Utils;

public class GUIClient extends JFrame {
	private static final long serialVersionUID = 1L;

	public JButton b_replay = new JButton(MainClass.rl.getImage("replay"));
	public JButton b_skip = new JButton(MainClass.rl.getImage("next"));

	public JButton b_settings = new JButton();
	public JButton b_mute = new JButton(MainClass.rl.getImage("unmute"));
	public JLabel l_online = new JLabel("Online: ");
	public JLabel l_status = new JLabel("Status: ");
	public JLabel l_timer = new JLabel("00:00|00:00", SwingConstants.CENTER);
	public JSlider s_volume = new JSlider();
	
	public SlideLinePanel sl_currentSong = new SlideLinePanel();

	public GUIClient(String name) {

		this.getContentPane().setBackground(Color.lightGray);
		this.setTitle(name);
		this.setSize(400, 90);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		this.setIconImage(MainClass.rl.icon);

		this.add(sl_currentSong);
		sl_currentSong.setBounds(1, 14, 392, 20);

		this.add(l_status);
		l_status.setBounds(192, -3, 200, 20);
		l_status.setFont(Utils.STANDART_FONT);
		l_status.setHorizontalAlignment(JLabel.RIGHT);

		this.add(b_mute);
		b_mute.setBounds(61, 35, 30, 25);

		this.add(b_replay);
		b_replay.setBounds(1, 35, 30, 25);
		b_replay.addActionListener(e -> {
			ClientInputReader.command = NetCodes.TS_REPLAY;
		});

		this.add(b_skip);
		b_skip.setBounds(31, 35, 30, 25);
		b_skip.addActionListener(e -> {
			ClientInputReader.command = NetCodes.TS_SKIP;
		});

		this.add(l_online);
		l_online.setBounds(3, -3, 150, 20);
		l_online.setFont(Utils.STANDART_FONT);

		this.add(l_timer);
		l_timer.setBounds(254, 39, 105, 20);
		l_timer.setFont(Utils.TIMER_FONT);

		this.add(s_volume);
		s_volume.setMaximum(90);
		s_volume.setValue(50);
		s_volume.setBounds(91, 35, 125, 25);
		s_volume.setBackground(Color.lightGray);

		b_mute.addActionListener(e -> {
			Client.mute();
		});

		s_volume.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				VolumeController.SetVolume(s_volume.getValue());
			}
		});

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

		this.add(b_settings);

		b_settings.setBounds(363, 35, 30, 25);
		b_settings.setIcon(MainClass.rl.getImage("gear"));
		b_settings.addActionListener(e -> {
			Settings.mainframe.setLocationRelativeTo(this);
			Settings.mainframe.setVisible(true);
			b_settings.setEnabled(false);
			this.setEnabled(false);
		});
	}

	public void showGUI() {
		this.setVisible(true);
		this.createBufferStrategy(2);
	}

	// public void ShakeOff() {
	// if (!Client.isPaised && Settings.isShaking) {
	// Point last = this.getLocation();
	// this.setLocation(last.x + Utils.getRandom(2), Utils.getRandom(2));
	// Utils.sleep(20);
	// this.setLocation(last);
	//
	// }
	// }

}
