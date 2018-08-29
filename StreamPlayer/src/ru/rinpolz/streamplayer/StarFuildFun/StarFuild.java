package ru.rinpolz.streamplayer.StarFuildFun;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.VolatileImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;

public class StarFuild {

	static VolatileImage inag;

	public static float speedMul = 0;

	public StarFuild() {

		ArrayList<SpaceObject> arrey = new ArrayList<>();

		arrey.add(new Nebula());

		for (int i = 0; i < 400; i++) {
			arrey.add(new Star());

		}

		for (int i = 0; i < 30; i++) {
			arrey.add(new Nebula());

		}

		S_Pannel p = new S_Pannel();
		JFrame f = new JFrame();

		f.setSize(500, 500);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(p);
		f.setVisible(true);

		if (inag == null) {
			GraphicsConfiguration gConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
					.getDefaultConfiguration();
			inag = gConfig.createCompatibleVolatileImage(400, 400);

		}

		Graphics2D graph = (Graphics2D) inag.getGraphics();
		graph.translate(inag.getWidth() / 2, inag.getHeight() / 2);

		p.init();

		Timer t = new Timer(1000 / 70, new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				graph.setColor(new Color(1, 1, 1, 23));

				graph.fillRect(-inag.getWidth() / 2, -inag.getHeight() / 2, inag.getWidth(), inag.getHeight());

				for (SpaceObject spo : arrey) {

					spo.update();
					graph.setColor(spo.GetColor());
					graph.fillRect(spo.getX() - spo.getH() / 2, spo.getY() - spo.getH() / 2, spo.getH(), spo.getH());

				}

				p.Render();
			}
		});
		t.start();

	}

}
