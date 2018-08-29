package ru.rinpolz.streamplayer.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import ru.rinpolz.streamplayer.mainlogic.Client;
import ru.rinpolz.streamplayer.mainlogic.MainClass;
import ru.rinpolz.streamplayer.mainlogic.Server;
import ru.rinpolz.streamplayer.utill.Utils;

public class SlideLinePanel extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	private Color backColor;
	static BufferStrategy bfs;
	private Graphics g;
	private byte[] samples;
	private String line = " ";

	private boolean init = false;
	private boolean isPaint = false;
	private boolean isRunning = false;

	private boolean back = false;

	private float cprogress = 0;
	private int Destprogress = 0;

	private float curPresset = 0;
	private int presset = -1;

	private int x = 0;
	private int summ = 0;
	private int arreyOffset = 0;

	private int stringWidth = 0;
	private boolean updateStingSize = true;

	public SlideLinePanel() {
		this.setBackground(Color.DARK_GRAY);
		this.setIgnoreRepaint(true);
		this.setFocusable(false);
		this.setEnabled(false);

	}

	public void render() {

		try {

			g = bfs.getDrawGraphics();

			g.setFont(Utils.SLIDER_FONT);
			if (updateStingSize) {
				stringWidth = g.getFontMetrics().stringWidth(line);
				updateStingSize = false;
			}

			summ = norm(summ);
			backColor = new Color(summ, 50, 50, 128);
			g.setColor(backColor);

			g.fillRect(0, 0, this.getWidth(), this.getHeight());

			cprogress = (float) Utils.lerp(cprogress, Destprogress, 0.05);

			g.setColor(Utils.PROGRESS_COLOR);
			g.fillRect(0, 3, (int) cprogress, 14);

			summ = 0;

			if (samples != null) {
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
						int l = Math.abs(samples[i]) / 8;
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

				if (presset > -1 && this.isEnabled()) {
					curPresset = (float) Utils.lerp(curPresset, presset, 0.4);
					g.setColor(Utils.PRESET_COLOR);
					g.fillRect((int) cprogress, 5, (int) (curPresset - cprogress), 10);
				}
				g.setColor(Utils.DEST_COLOR);
				g.fillRect((int) (cprogress - 3), 1, 5, 18);
			}

			///////

			if (summ > 600) {
				if (!MainClass.isRemote) {
					Client.gui.ShakeOff();
				} else {
					Server.gui.ShakeOff();
				}
			}

			g.setColor(Color.white);
			g.drawString(line, (int) x, Utils.SLIDER_FONT.getSize());

			if (isPaint) {

				if (stringWidth < 393) {
					if (!back) {
						if (x < 393 - stringWidth) {
							x++;
						} else {
							back = true;
						}
					} else {
						if (x > 0) {
							x--;
						} else {
							back = false;
						}
					}

				} else {
					if (!back) {
						if (x < 30) {
							x++;
						} else {
							back = true;
						}
					} else {
						if (x > stringWidth * -1 + 362) {
							x--;
						} else {
							back = false;
						}

					}

				}
				isPaint = false;
			}

			bfs.show();
			g.dispose();

		} catch (Exception e) {
			init = false;
			e.printStackTrace();
		}

	}

	// Setters
	public void setPresset(int pr) {
		presset = pr > 392 || pr < 0 ? -1 : pr;

	}

	public void clearPreset() {
		presset = -1;
	}

	public void setValue(int ps) {
		Destprogress = ps > 392 || ps < 0 ? -1 : ps;

	}

	public void UpdateSpec(byte[] indat) {
		if (indat.length >= 404) {
			this.samples = indat;
		}
	}

	public void setRunning(boolean b) {
		isRunning = b;
	}

	public void setName(String s) {
		line = s.substring(0, s.lastIndexOf("."));
		updateStingSize = true;

	}

	// Getters
	public int getValue() {
		return Destprogress;
	}

	public boolean isInit() {
		return init;
	}

	// local
	public void init() {

		// todo make reinit

		try {
			createBufferStrategy(2);
			bfs = getBufferStrategy();
			init = true;
			repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		while (isRunning) {
			if (init) {
				this.isPaint = true;
				render();
			}
			Utils.sleep(42);

		}
	}

	public void resetAll(boolean b) {
		if (b) {
			x = 0;
			back = false;
		}
		samples = new byte[404];
	}

	public int norm(int i) {
		i = i / 14 + 50;
		if (i > 255) {
			return 255;
		}
		return i;

	};

}
