package ru.rinpolz.streamplayer.graphics;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Visualizer {

	public Visualizer() {

		JFrame f = new JFrame("Work-in-progress");
		VisualWorker work = new VisualWorker();

		f.setSize(400, 400);
		f.setLocationRelativeTo(null);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		f.setVisible(true);
		f.setLayout(null);
		f.add(work);
		
		work.setBounds(0, 0, 400, 400);
		work.init();

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				e.getWindow().dispose();
				work.kill();
			}
		});
	}
}
