package ru.rinpolz.streamplayer.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import ru.rinpolz.streamplayer.graphics.VisualWorker;
import ru.rinpolz.streamplayer.mainlogic.Client;
import ru.rinpolz.streamplayer.mainlogic.MainClass;
import ru.rinpolz.streamplayer.mainlogic.Server;
import ru.rinpolz.streamplayer.utill.Utils;

public class SlideLinePanel extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	static BufferStrategy bf;

	public byte[] arr;
	public String line = " ";

	public boolean init = false;
	boolean isPaint = false;

	public boolean isRunning = false;
	public boolean back = false;

	Color pColor = new Color(245, 155, 5, 130);
	
	Color dColor = new Color(255, 5, 0, 200);

	int presset = -1;

	Color prColor = new Color(255, 230, 5, 130);
	Color bprColor = new Color(15, 91, 255, 150);

	float cprogress = 0;
	int Destprogress = 0;

	int x = 0;
	int summ = 0;
	int arreyOffset = 0;

	public SlideLinePanel() {
		this.setBackground(Color.DARK_GRAY);
		this.setIgnoreRepaint(true);
		this.setFocusable(false);
		this.setEnabled(false);

	}

	public void render() {

		try {
			Graphics g = bf.getDrawGraphics();
			// ( ͡° ͜ʖ ͡° )
			
			summ = norm(summ);
			VisualWorker.CreateParticles(summ);

			g.setColor(new Color(summ, 50, 50, 128));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());

			cprogress = (float) Utils.lerp(cprogress, Destprogress, 0.05);
			g.setColor(pColor);
			g.fillRect(0, 3, (int) cprogress, 14);

			if (presset > -1&&this.isEnabled()) {
				if (presset > cprogress) {
					g.setColor(prColor);
				} else {
					g.setColor(bprColor);
				}
				g.fillRect((int) cprogress, 5, (int) (presset - cprogress), 10);
			}
			g.setColor(dColor);
			g.fillRect((int) (cprogress - 3), 1, 5, 18);

			summ = 0;

			if (arr != null) {
				// Visual
				int off = -3;
				boolean flag = true;

				if (arreyOffset < 10) {
					arreyOffset += 2;
				} else {
					arreyOffset = 0;
				}

				for (int i = arreyOffset; i < 404; i++) {

					if (i % 2 != 0) {
						int l = Math.abs(arr[i]) / 8;
						summ += l;
						if (flag) {
							g.setColor(Color.orange);
							g.fillRect(off, 9 - l, 2, l + 1);
							flag = false;
							off += 4;
						} else {
							g.setColor(Color.red);
							g.fillRect(off, 10, 2, l + 1);
							flag = true;
						}

					}

				}

			}

			///////

			if (summ > 600) {
				if (!MainClass.isRemote) {
					Client.gui.ShakeOff();
				} else {
					Server.gui.ShakeOff();
				}
			}

			g.setFont(Utils.SLIDER_FONT);
			g.setColor(Color.white);
			g.drawString(line, (int) x, Utils.SLIDER_FONT.getSize());

			if (isPaint) {
				if (!back) {

					if (x < this.getWidth()) {
						x++;
					} else {
						back = true;
					}

				} else {

					if (x > g.getFontMetrics().stringWidth(line) * -1) {
						x--;
					} else {
						back = false;
					}
				}
				isPaint = false;
			}

			bf.show();
			g.dispose();

		} catch (Exception e) {
			init = false;
			e.printStackTrace();
		}

	}

	public void setPresset(int pr) {
		presset = pr;
	}

	public void clearPreset() {
		presset = -1;
	}

	public void setValue(int ps) {

		if (ps > 392 || ps < 0) {
			Destprogress = 0;
		} else {
			Destprogress = ps;
		}

	}

	public int getValue() {
		return Destprogress;
	}

	public void init() {
		createBufferStrategy(2);
		bf = getBufferStrategy();
		init = true;
		repaint();

	}

	@Override
	public void run() {

		while (isRunning) {
			if (init) {
				this.isPaint = true;
				render();
			}
			Utils.sleep(45);

		}
	}

	public void UpdateSpec(byte[] indat) {
		this.arr = indat;

	}

	public void resetAll(boolean b) {
		if (b) {
			x = 0;
			back = false;
		}
		arr = new byte[404];
	}

	public void setName(String s) {
		line = s.substring(0, s.lastIndexOf("."));
	}

	public int norm(int i) {
		i = i / 14 + 50;
		if (i > 255) {
			return 255;
		}
		return i;

	};

}
