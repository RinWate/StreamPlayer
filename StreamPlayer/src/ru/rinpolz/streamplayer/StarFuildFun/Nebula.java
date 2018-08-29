package ru.rinpolz.streamplayer.StarFuildFun;

import java.awt.Color;
import java.util.Random;

public class Nebula implements SpaceObject {

	Color starColor;
	Random r = new Random();

	float x = 2;
	float y = 2;

	int live = 100;

	float speed;

	int rad = 0;

	public void ReInit() {

		live = r.nextInt(100) + 70;

		speed = 0.0001f;

		starColor = new Color(r.nextInt(254), r.nextInt(254), r.nextInt(254), r.nextInt(70));

		x = r.nextInt(64) - r.nextInt(64);
		y = r.nextInt(64) - r.nextInt(64);

		rad = r.nextInt(10);

		if (x == 0 || y == 0) {

			ReInit();
		}

		rad = 1;
	}

	public Nebula() {

		ReInit();

	}

	@Override
	public void update() {
		x *= 1.00003f + speed + StarFuild.speedMul;
		y *= 1.00003f + speed + StarFuild.speedMul;

		if (Math.abs(x + y) > 128) {

			speed = r.nextInt(4) * 0.01f;
			x = r.nextInt(64) - r.nextInt(64) + 1;
			y = r.nextInt(64) - r.nextInt(64) + 1;
			rad = r.nextInt(10);

		}

		if (live < 0) {

			ReInit();

		}

		live--;
		rad += (int) (Math.abs(x + y) / 90);

	}

	@Override
	public int getX() {

		return (int) x;
	}

	@Override
	public int getY() {

		return (int) y;
	}

	@Override
	public Color GetColor() {

		return starColor;
	}

	public int getH() {

		return rad;
	}

}
