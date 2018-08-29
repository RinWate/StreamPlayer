package ru.rinpolz.streamplayer.StarFuildFun;

import java.awt.Canvas;
import java.awt.image.BufferStrategy;

public class S_Pannel extends Canvas {

	BufferStrategy bf;

	private static final long serialVersionUID = 1L;

	void Render() {

		bf.getDrawGraphics().drawImage(StarFuild.inag, 0, 0, this.getWidth(), this.getHeight(), null);

		bf.show();
		
	}

	public void init() {

		createBufferStrategy(2);

		bf = getBufferStrategy();
	}
}
