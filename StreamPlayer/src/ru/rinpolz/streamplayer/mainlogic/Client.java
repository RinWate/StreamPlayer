package ru.rinpolz.streamplayer.mainlogic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeoutException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JOptionPane;

import ru.rinpolz.streamplayer.gui.GUIClient;
import ru.rinpolz.streamplayer.network.ClientInputReader;
import ru.rinpolz.streamplayer.network.NetCodes;
import ru.rinpolz.streamplayer.network.PacketTrack;
import ru.rinpolz.streamplayer.network.StatUsParser;
import ru.rinpolz.streamplayer.settingsIO.FileLoader;
import ru.rinpolz.streamplayer.utill.Utils;

public class Client extends Thread {
	VolumeController controll = new VolumeController(false);

	// Максимальный размер пакета
	

	int nonReadebleCycles = 0;
	int uiUpdate = 0;
	byte retry = 0;

	SourceDataLine clipSDL;
	ClientInputReader input;
	AudioFormat decodedFormat;

	static int last = 0;
	static boolean isMute = false;

	public static boolean isPaised = false;

	boolean statuschanged = false;

	SocketChannel sc;
	boolean isConnected = false;
	boolean isError = false;

	float samplerate = 0;

	public static GUIClient gui = new GUIClient("StreamPlayer Client");
	static FloatControl volume;

	byte errorid = 0;

	// DBG
	long readed = 0;
	long acceped = 0;
	long resets = 0;
	long skipped = 0;

	Thread lineupdater = new Thread(gui.sl_currentSong);
	StatUsParser parser = new StatUsParser();

	@Override
	public void run() {
		lineupdater.setPriority(NORM_PRIORITY);
		this.setPriority(MAX_PRIORITY);
		this.setName("Client player");

		MainClass.isRemote = false;
		MainClass.login.setButtonStatus(false);
		gui.sl_currentSong.setRunning(true);

		lineupdater.start();
		retry = 3;
		while (retry <= 3) {
			try {
				System.out.println("Recconected");
				isConnected = false;
				sc = SocketChannel.open();
				sc.socket().connect(new InetSocketAddress(MainClass.ip, MainClass.port), 3000);

				isConnected = true;

				MainClass.login.setButtonStatus(false);
				if (sc.isConnected()) {
					isError = false;
					MainClass.login.setVisible(false);
					gui.showGUI();
					if (!gui.sl_currentSong.isInit()) {
						gui.sl_currentSong.init();

					}

					MainClass.login.dispose();
					gui.s_volume.setValue(FileLoader.CurrentSettings.volume);

				}

				input = new ClientInputReader(sc);

				while (!isError) {
					PacketTrack startPack = null;
					gui.l_status.setText("Status: Synchronization");

					// TODO loop !infinity! may lagged
					while (startPack == null) {
						startPack = input.getData();
						Utils.sleep(17);
					}

					parser.Parse(startPack.stringData);
					samplerate = startPack.samplerate;
					gui.sl_currentSong.resetAll(true);
					gui.sl_currentSong.setName(parser.getTite());
					matchVersion(parser.getVersion());
					retry = 0;

					decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, samplerate, 16, 2, 4, 44100.0f,
							false);
					DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
					clipSDL = (SourceDataLine) AudioSystem.getLine(info);
					clipSDL.open(decodedFormat);
					volume = (FloatControl) clipSDL.getControl(FloatControl.Type.MASTER_GAIN);
					clipSDL.start();

					VolumeController.current_volume = -80f;
					VolumeController.updVolme();

					nonReadebleCycles = 0;

					while (!isError) {
						try {

						
							skipped = acceped - readed;

							PacketTrack i = input.getData();

							if (i != null) {

								parser.Parse(i.stringData);

								nonReadebleCycles = 0;
								isPaised = i.netCode == NetCodes.PAUSED;
								gui.l_timer.setText(parser.getTimeline());
								gui.l_status.setText(parser.getStatus());

								gui.sl_currentSong.setValue(i.progress);

								uiUpdate++;

								/// ТУТ КЭКК
								if (i.netCode == NetCodes.ENDED) {
									Client.gui.sl_currentSong.resetAll(false);
									break;
								}

								// Если трек проигрывается

								// TODO ТУТ
								if (i.netCode != NetCodes.PAUSED && i.leg != -1) {
									clipSDL.start();
									clipSDL.write(i.data, 0, i.leg);
									gui.sl_currentSong.UpdateSpec(i.data);

								} else {

									VolumeController.current_volume = -80;
									gui.sl_currentSong.resetAll(false);
									clipSDL.drain();
									uiUpdate = 25;
								}

								if (uiUpdate >= 25) {
									gui.l_online.setText("Online: " + i.num_of_clients + getWord(i.num_of_clients));
									gui.sl_currentSong.setName(parser.getTite());

									// gui.pr_bar.setToolTipText(i.filename);
									// if (gui.pr_bar.isIndeterminate()) {
									// gui.pr_bar.setIndeterminate(false);
									// }

									uiUpdate = 0;

								}
								readed++;
							} else {
								nonReadebleCycles++;

								Utils.sleep(20);
								if (nonReadebleCycles > 5) {
									gui.sl_currentSong.resetAll(false);
								}

								if (nonReadebleCycles > 100) {
									gui.l_status.setText("No data available...");
									// gui.pr_bar.setIndeterminate(true);

								}
								if (nonReadebleCycles > 400) {
									gui.sl_currentSong.resetAll(true);
									throw new TimeoutException();
								}

							}

						} catch (Exception e) {
							e.printStackTrace();
							isError = true;

						}

					}

					gui.sl_currentSong.setValue(0);
					clipSDL.close();

				}

			} catch (UnknownHostException e) {
				errorid = 0;
				e.printStackTrace();
			} catch (SocketTimeoutException e) {
				errorid = 1;
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
				errorid = 2;
			} catch (ConnectException e) {
				e.printStackTrace();
				errorid = 3;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (retry >= 3) {

					if (input != null) {
						input.stopThead();
					}

					// gui.pr_bar.setIndeterminate(false);

					ShowErrorMessage(errorid);
				}

				gui.l_online.setText("Online: No info");
				gui.l_status.setText(retry + 1 + "/3 Try to connect");
				retry++;

			}
		}

		isError = true;
		gui.l_status.setText("Status: error...");
		gui.sl_currentSong.setRunning(false);
		gui.dispose();
		MainClass.login.setVisible(true);
		MainClass.login.setButtonStatus(true);
	}

	public void matchVersion(String version) {
		if (!version.equalsIgnoreCase(Utils.VERSION)) {
			JOptionPane.showMessageDialog(gui,
					"WARNING: Your client version does not match the server's. \nIt cause some stability problems or crashes "
							+ "\nClient Version: " + Utils.VERSION + "\nServer Version: " + version
							+ "\nPlease, update program: http://vk.com",
					"Version Checker", JOptionPane.INFORMATION_MESSAGE);
			gui.setTitle("StreamPlayer Client (OLD)");

		}
	}

	///////////////////// net
	public static Object convertFromBytes(ByteBuffer buffer) throws IOException, ClassNotFoundException {

		ByteArrayInputStream bis = new ByteArrayInputStream(buffer.array());
		ObjectInput in = new ObjectInputStream(bis);
		return in.readObject();

	}

	public String getWord(int num) {
		if (num == 1) {
			return " listener";
		} else {
			return " listeners";
		}
	}

	public void ShowErrorMessage(int id) {

		switch (id) {
		case 0:
			MainClass.login.status.setText("UnknownHost");
			JOptionPane.showMessageDialog(gui, "UnknownHost", "Error", JOptionPane.ERROR_MESSAGE);
			break;
		case 1:
			MainClass.login.status.setText("Timeout");
			JOptionPane.showMessageDialog(gui, "Timeout", "Error", JOptionPane.ERROR_MESSAGE);
			break;
		case 2:
			MainClass.login.status.setText("Playback error");
			JOptionPane.showMessageDialog(gui, "Playback error", "Error", JOptionPane.ERROR_MESSAGE);
			break;
		case 3:
			MainClass.login.status.setText("Connection lost");
			JOptionPane.showMessageDialog(gui, "Connection lost", "Error", JOptionPane.ERROR_MESSAGE);
			break;
		case 4:
			MainClass.login.status.setText("Connection refused");
			JOptionPane.showMessageDialog(gui, "Connection refused", "Error", JOptionPane.ERROR_MESSAGE);
			break;
		}
	}

	public static void moveVolume(boolean b) {
		if (b) {
			if (gui.s_volume.getValue() + 1 < gui.s_volume.getMaximum()) {
				gui.s_volume.setValue(gui.s_volume.getValue() + 1);
			} else {
				System.out.println("Max");
			}

		} else {
			if (gui.s_volume.getValue() - 1 > gui.s_volume.getMinimum()) {
				gui.s_volume.setValue(gui.s_volume.getValue() - 1);
			} else {
				System.out.println("Min");
			}
		}
	}

	public static void mute() {
		if (!isMute) {
			gui.s_volume.setEnabled(false);
			last = gui.s_volume.getValue();
			gui.s_volume.setValue(0);
			gui.b_mute.setIcon(MainClass.rl.getImage("mute"));
			isMute = true;
		} else {
			gui.s_volume.setEnabled(true);
			gui.s_volume.setValue(last);
			gui.b_mute.setIcon(MainClass.rl.getImage("unmute"));
			isMute = false;
		}
	}

	public boolean isConnected() {
		return isConnected;
	}
}