package ru.rinpolz.streamplayer.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import ru.rinpolz.streamplayer.utill.Utils;

public class Particles {

	static Random r = new Random();

	int rad = 2;

	float x = 195;
	float y = 290;

	Color overlap;
	Color smoke = new Color(100, 100, 100, 80);

	float speedY;
	float speedX;

	boolean cloud;

	public void draw(Graphics g) {

		Graphics gd = g.create();

		if (cloud) {

			g.setColor(smoke);
			g.fillRect((int) x, (int) y, rad, rad);

		} else {
			g.setColor(Color.orange);
			g.fillOval((int) x, (int) y, rad, rad);
			g.setColor(overlap);
			g.fillOval((int) x, (int) y, rad, rad);

			if (rad > 8) {

				g.setColor(smoke);
				g.fillOval((int) (x + rad / 2) - 3, (int) (y + rad / 2) - 3, 6, 6);

			}

		}

		gd.dispose();

	}

	public Particles(int volume) {

		if (r.nextBoolean() && r.nextBoolean()) {
			cloud = true;
		}

		if (cloud) {
			speedX = (Utils.getRandom(volume)) * 0.02f;
			speedY = volume * 0.03f * -1;
			rad += (int) (volume / 10);
		} else {
			overlap = new Color(255, 0, 0, volume);
			rad += (int) (volume / 20);
			speedX = (Utils.getRandom(volume)) * 0.02f;
			speedY = volume * 0.07f * -1;
		}

	}

	public void update() {

		if (!cloud) {
			speedY += 0.3f;
		}

		x += speedX;
		y += speedY;

		if (y > 400 || y < 0 || x > 400 || x < 0) {
			VisualWorker.items.remove(this);
		}
	}
}
