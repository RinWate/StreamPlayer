package ru.rinpolz.streamplayer.equalizer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import ru.rinpolz.streamplayer.mainlogic.Server;
import ru.rinpolz.streamplayer.settingsIO.FileLoader;
import ru.rinpolz.streamplayer.utill.Utils;

public class Equalizer {

	public static ArrayList<JSlider> sliderArrey = new ArrayList<>();
	public static ArrayList<JCheckBox> ChArrey = new ArrayList<>();
	public static ArrayList<JLabel> Jl_Arrey = new ArrayList<>();

	JButton b_selectAll = new JButton("SelectAll");
	JButton b_deselectAll = new JButton("DeselectAll");
	JButton b_invert = new JButton("Invert");

	JButton b_Scroller = new JButton("|");

	JButton b_max = new JButton("Max");
	JButton b_reset = new JButton("Reset");
	JButton b_min = new JButton("Min");

	JButton b_ramdom = new JButton("Random");

	JButton b_Save = new JButton("Save");

	static final String[] labels = { "25", "31", "40", "50", "63", "80", "100", "125", "160", "200", "250", "315",
			"400", "500", "630", "800", "1k", "1k3", "1k6", "2k", "2k5", "3k15", };

	int hg;

	JFrame frame;

	public Equalizer(JFrame f, int h) {
		frame = f;
		hg = h;

	}

	///// Equalizer
	public void initEquo() {

		System.err.println("Equalizer");

		b_selectAll.setBounds(1, hg + 135, 100, 20);
		b_selectAll.addActionListener(e -> {
			setCB(true);
		});
		frame.add(b_selectAll);

		b_invert.setBounds(1, hg + 155, 100, 20);
		b_invert.addActionListener(e -> {
			invert();

		});
		frame.add(b_invert);

		b_deselectAll.setBounds(1, hg + 175, 100, 20);
		b_deselectAll.addActionListener(e -> {
			setCB(false);
		});
		frame.add(b_deselectAll);

		b_Save.setBounds(1, hg + 195, 100, 20);

		frame.add(b_Save);
		b_Save.addActionListener(e -> {
			b_Save.setEnabled(false);
			RefreshEqualizer();
			FileLoader.saveSettings();
		});

		b_Scroller.setBounds(190, hg + 140, 20, 75);
		b_Scroller.setEnabled(false);
		b_Scroller.setBackground(Color.DARK_GRAY);
		b_Scroller.setForeground(Color.black);
		b_Scroller.addMouseWheelListener(e -> {

			if (e.isShiftDown()) {
				if (e.getWheelRotation() == -1) {
					MoveAll(1);
				} else {
					MoveAll(-1);
				}
			}

		});

		frame.add(b_Scroller);

		b_max.setBounds(294, hg + 135, 100, 20);
		b_max.addActionListener(e -> {
			setall(40);
		});
		frame.add(b_max);

		b_reset.setBounds(294, hg + 155, 100, 20);
		b_reset.addActionListener(e -> {
			setall(20);
		});
		frame.add(b_reset);

		b_min.setBounds(294, hg + 175, 100, 20);
		b_min.addActionListener(e -> {
			setall(0);
		});
		frame.add(b_min);

		b_ramdom.setBounds(294, hg + 195, 100, 20);
		b_ramdom.addActionListener(e -> {
			randomize();
		});
		frame.add(b_ramdom);

		for (int i = 0; i < 22; i++) {
			final int d;
			d = i;
			JSlider t_sl = new JSlider(SwingConstants.VERTICAL);
			t_sl.setBackground(Color.lightGray);
			t_sl.setBounds(18 * i, hg, 18, 100);
			t_sl.setFocusable(false);

			t_sl.setMaximum(40);
			t_sl.setMinimum(0);
			t_sl.setValue(20);

			JLabel l_Bust = new JLabel("0.0", SwingConstants.CENTER);

			l_Bust.setFont(Utils.CHECK_BOX_FONT);
			l_Bust.setBounds(t_sl.getX() - 2, hg - 12, 18, 18);

			Jl_Arrey.add(l_Bust);
			frame.add(l_Bust);

			t_sl.addMouseWheelListener(e -> {

				if (t_sl.isEnabled()) {
					if (e.getWheelRotation() == -1) {
						t_sl.setValue(t_sl.getValue() + 1);
					} else {
						t_sl.setValue(t_sl.getValue() - 1);
					}
				}

			});

			t_sl.addChangeListener(e -> {
				enebleSaveButton();
				Server.equalizer[d] = (float) Utils.map(t_sl.getValue(), 0, 40, -1.0f, 1.0f);
				l_Bust.setText(String.format(Locale.ENGLISH, "%.1f", Server.equalizer[d]));

			});

			frame.add(t_sl);
			sliderArrey.add(t_sl);

			JCheckBox c_cbx = new JCheckBox(labels[i], true);

			c_cbx.setBounds(t_sl.getX() - 4, hg + 100, 24, 40);

			c_cbx.setHorizontalTextPosition(SwingConstants.CENTER);
			c_cbx.setVerticalTextPosition(SwingConstants.BOTTOM);
			c_cbx.setFont(Utils.CHECK_BOX_FONT);
			c_cbx.setFocusable(false);
			c_cbx.setSelected(true);
			c_cbx.setContentAreaFilled(false);

			c_cbx.addActionListener(e -> {

				enebleSaveButton();

				if (c_cbx.isSelected()) {
					t_sl.setEnabled(true);
				} else {
					t_sl.setEnabled(false);
				}
			});

			frame.add(c_cbx);
			ChArrey.add(c_cbx);

		}

		setVisabilyty(false);

		System.out.println(sliderArrey.size());

		SetSlidersPos(FileLoader.CurrentSettings.sliders);
		SetCbxState(FileLoader.CurrentSettings.chekcboxes);

		b_Save.setEnabled(false);

	}

	public static void RefreshEqualizer() {
		byte offset = 0;
		for (JSlider jSlider : sliderArrey) {

			Server.equalizer[offset] = (float) Utils.map(jSlider.getValue(), 0, 40, -1.0f, 1.0f);

			offset++;
		}

	}

	public static void MoveAll(int how) {

		for (JSlider js : sliderArrey) {
			if (js.isEnabled()) {
				js.setValue(js.getValue() + how);

			}

		}
	}

	public static void randomize() {
		for (JSlider js : sliderArrey) {
			if (js.isEnabled()) {
				js.setValue(Utils.random.nextInt(40));

			}

		}

	}

	public static void setall(int h) {
		for (JSlider js : sliderArrey) {
			if (js.isEnabled()) {
				js.setValue(h);

			}

		}

	}

	public static void invert() {
		for (JCheckBox jCheckBox : ChArrey) {
			jCheckBox.doClick(0);

		}

	}

	public static void setCB(boolean state) {
		for (JCheckBox jCheckBox : ChArrey) {
			if (jCheckBox.isSelected() != state) {
				jCheckBox.doClick(0);
			}

		}

	}

	public void setVisabilyty(boolean state) {

		b_deselectAll.setVisible(state);
		b_selectAll.setVisible(state);
		b_invert.setVisible(state);

		b_Scroller.setVisible(state);

		b_max.setVisible(state);
		b_reset.setVisible(state);
		b_min.setVisible(state);

		b_ramdom.setVisible(state);

		for (int i = 0; i < 22; i++) {
			ChArrey.get(i).setVisible(state);
			Jl_Arrey.get(i).setVisible(state);
			sliderArrey.get(i).setVisible(state);

		}

	}

	////// Settings

	public static void SetSlidersPos(int[] data) {
		for (int i = 0; i < 22; i++) {
			sliderArrey.get(i).setValue(data[i]);

		}

	}

	public static void SetCbxState(boolean[] states) {
		for (int i = 0; i < 22; i++) {
			if (ChArrey.get(i).isSelected() != states[i]) {
				ChArrey.get(i).doClick(0);
			}

		}

	}

	public static int[] GetSliderPos() {
		int[] arrey = new int[22];
		for (int i = 0; i < sliderArrey.size(); i++) {
			arrey[i] = sliderArrey.get(i).getValue();
		}
		return arrey;

	}

	public static boolean[] GetCheckboxes() {
		boolean[] arrey = new boolean[22];
		for (int i = 0; i < sliderArrey.size(); i++) {
			arrey[i] = ChArrey.get(i).isSelected();

		}
		return arrey;
	}

	public void enebleSaveButton() {
		b_Save.setEnabled(true);
	}

}
