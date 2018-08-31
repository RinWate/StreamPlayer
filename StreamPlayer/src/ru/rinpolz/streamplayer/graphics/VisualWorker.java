package ru.rinpolz.streamplayer.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ImageIcon;

import ru.rinpolz.streamplayer.util.Utils;

public class VisualWorker extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	Random r = new Random();

	static int shake = 0;
	boolean live = true;

	public static CopyOnWriteArrayList<Particles> items = new CopyOnWriteArrayList<>();

	Image i = new ImageIcon(getClass().getResource("VulcanoPucano.png")).getImage();

	BufferStrategy bf;
	boolean init = false;

	public void render() {
		Graphics g = bf.getDrawGraphics();
		g.setColor(new Color(0, 0, 0, 40));
		g.fillRect(0, 0, getWidth(), getHeight());

		if (shake > 0) {
			g.setColor(Color.ORANGE);
			g.translate(Utils.getRandom(4), Utils.getRandom(4));

			shake--;
		}

		for (Particles particles : items) {
			particles.update();
			particles.draw(g);
		}
		
		g.drawImage(i, 145, 270, 100, 100, null);

		if (shake > 0) {
			g.setColor(new Color(255, 162, 21, 60));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.fillOval(171, 270, 50, 45);
		}
		bf.show();
		g.dispose();
	}

	public void init() {
		createBufferStrategy(2);
		bf = getBufferStrategy();
		init = true;
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (live) {
			try {
				Thread.sleep(1000 / 50);
				if (init && live) render();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void CreateParticles(int volume) {
		if (volume > 140) {
			shake = 3;
		}

		if (items.size() < 100 && volume > 53) {
			items.add(new Particles(volume));
		}
	}

	public void kill() {
		live = false;
	}
}