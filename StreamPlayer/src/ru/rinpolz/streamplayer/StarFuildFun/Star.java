package ru.rinpolz.streamplayer.StarFuildFun;

import java.awt.Color;
import java.util.Random;

public class Star implements SpaceObject {

	Color starColor;
	Random r = new Random();
	float x;
	float y;
	
	int live=100;

	float speed;

	int rad = 0;

	public void ReInit() {

		
		live=r.nextInt(100)+70;
		
		speed = r.nextInt(4) * 0.01f;

		starColor = new Color(r.nextInt(254), r.nextInt(254), r.nextInt(254));

		x = r.nextInt(64) - r.nextInt(64);
		y = r.nextInt(64) - r.nextInt(64);

		
		if(x==0||y==0) {
			
			ReInit();
		}
		
		rad = 1;
	}

	public Star() {

		ReInit();
	}

	@Override
	public void update() {
		x *= 1.002  + StarFuild.speedMul;
		y *= 1.002  + StarFuild.speedMul;

		
		if (live<0) {

			ReInit();

		}

		rad = (int) (Math.abs(x + y) / 50);
		rad += 1;
		live--;
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
