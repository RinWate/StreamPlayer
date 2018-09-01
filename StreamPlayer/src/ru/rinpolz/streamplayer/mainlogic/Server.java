package ru.rinpolz.streamplayer.mainlogic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

import javazoom.spi.mpeg.sampled.convert.DecodedMpegAudioInputStream;
import ru.rinpolz.streamplayer.equalizer.Equalizer;
import ru.rinpolz.streamplayer.gui.GUIServer;
import ru.rinpolz.streamplayer.network.ClientConnection;
import ru.rinpolz.streamplayer.network.ClientListener;
import ru.rinpolz.streamplayer.network.NetCodes;
import ru.rinpolz.streamplayer.network.PacketTrack;
import ru.rinpolz.streamplayer.settingsIO.FileLoader;
import ru.rinpolz.streamplayer.trackControll.FileList;
import ru.rinpolz.streamplayer.trackControll.Track;
import ru.rinpolz.streamplayer.utill.Utils;

public class Server extends Thread {
	public static int SIZE = 8192;
	StringBuffer strigBuffer = new StringBuffer();

	public static volatile float[] equalizer = new float[32];

	String duration = "";
	VolumeController controll = new VolumeController(true);

	ByteBuffer sendbuffer = ByteBuffer.allocate(SIZE);
	ByteBuffer inputCommandBuffer = ByteBuffer.allocate(1);

	public static int lastvolume = 0;

	static boolean isMute = false;
	public static boolean isReplaed = false;
	public static boolean isSet = false;

	public static boolean isEnded = false;
	public static boolean isPaused = false;
	public static boolean isSkip = false;
	public static boolean isPosCha = false;

	public static byte SERVER__STATUS = 0;

	public static ClientConnection currentConnwct;
	public static boolean isSended = true;

	/////// TrackControll
	public static long HowManySkip = 0;
	public static int trckLegit = 0;
	static long trackSizeInmicros = 0;

	///// Titleandsomeshit
	static String status = "";
	static String title = "";
	static String timeline = "";

	public static File CurrentTrack;
	public static FileList playlist;

	/////// I|O
	public static AudioInputStream in;
	public static SourceDataLine Output;
	AudioFormat decodedFormat;
	byte[] data;
	int num;
	float sampler;
	public static GUIServer gui = new GUIServer("StreamPlayer Server on: " + MainClass.port);
	public static boolean isOpen;
	Thread lineupdater = new Thread(gui.sl_currentSong);

	public static FloatControl volume;
	static int noskipcycles = 0;

	public void run() {

		lineupdater.setPriority(NORM_PRIORITY);
		this.setPriority(MAX_PRIORITY);
		this.setName("Server-Main");

		MainClass.isRemote = true;
		MainClass.login.setVisible(false);
		playlist = new FileList();
		gui.sl_currentSong.setRunning(true);
		lineupdater.start();

		while (playlist.fr.isVisible()) {
			Utils.sleep(100); // TODO 400
		}

		ClientListener listener = new ClientListener();
		listener.start();

		while (!isOpen) {
			Utils.sleep(300);
		}

		gui.showGUI();

		gui.sl_currentSong.init();
		gui.s_volume.setValue(FileLoader.CurrentSettings.volume);

		while (isOpen) {

			Utils.sleep(150);

			if (!FileList.filelist.isEmpty()) {
				try {
					if (CurrentTrack == null) {
						Track selected = playlist.getSong(isSet);
						CurrentTrack = new File(selected.file.getPath());
						selected = null;
					}

					if (gui.hasReplay && !isSet) {
						playFile(CurrentTrack);
					} else {
						if (!isReplaed) {
							Track selected = playlist.getSong(isSet);
							CurrentTrack = new File(selected.file.getPath());
						} else {
							isReplaed = false;
						}
						playFile(CurrentTrack);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Utils.sleep(777);
			}
		}
	}

	public void playFile(File f) {
		gui.l_timing.setText("--:--");
		gui.updateDeleteButtonState();

		isEnded = false;

		title = f.getName();
		gui.l_status.setText(status);
		gui.sl_currentSong.setName(f.getName());
		gui.sl_currentSong.resetAll(true);

		try {

			isSkip = false;
			isSet = false;
			gui.sl_currentSong.setValue(0);

			data = new byte[5512];
			num = 0;

			in = AudioSystem.getAudioInputStream(f);
			sampler = in.getFormat().getSampleRate();
			decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampler, 16, 2, 4, 44100.0f, false);

			DecodedMpegAudioInputStream din = new DecodedMpegAudioInputStream(decodedFormat, in);

			equalizer = (float[]) (din.properties().get("mp3.equalizer"));

			// Equalizer.RefreshEqualizer();

			duration = Utils.getDuration(f);
			trackSizeInmicros = Utils.getMs(f);
			timeline = "00:00|" + duration;

			HowManySkip = 0;

			DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);

			Output = (SourceDataLine) AudioSystem.getLine(info);
			Output.open(decodedFormat);

			volume = (FloatControl) Output.getControl(FloatControl.Type.MASTER_GAIN);

			SMS.say("Clip started");

			trckLegit = in.available();
			VolumeController.current_volume = -80f;
			VolumeController.updVolme();
			Output.start();

			gui.sl_currentSong.setEnabled(true);

			while (num != -1 && !isSkip) {

				gui.l_online.setText(Utils.ONLINE + ClientListener.ÑlientsConnections.size()
						+ Utils.getWord(ClientListener.ÑlientsConnections.size()));

				if (HowManySkip != 0) {
					isPosCha = true;
					long skiping = HowManySkip;
					HowManySkip = 0;

					VolumeController.mute();
					VolumeController.updVolme();

					if (trckLegit - in.available() > skiping) {
						in = AudioSystem.getAudioInputStream(f);
						din = new DecodedMpegAudioInputStream(decodedFormat, in);
						equalizer = (float[]) (din.properties().get("mp3.equalizer"));
						Equalizer.RefreshEqualizer();

					} else {
						skiping -= trckLegit - in.available();
					}

					while (skiping != 0) {
						Thread.sleep(20);
						long s = in.skip(skiping);
						if (s == 0) {
							break;
						}
						skiping -= s;
					}

					VolumeController.unmute();
					VolumeController.updVolme();
				}

				if (isPaused) {
					forAllClients(createPack());

					gui.sl_currentSong.resetAll(false);
					status = Utils.STATUS + Utils.PAUSE;
					gui.l_status.setText(status);

					if (Output.isRunning()) {
						Output.drain();
						Output.stop();
					}

					Utils.sleep(70);

				} else {
					if (!Output.isRunning()) {
						Output.start();
					}

					gui.sl_currentSong.UpdateSpec(data);
					Output.write(data, 0, num);

					num = din.read(data);

					forAllClients(createPack());
					isPosCha = false;

					status = Utils.STATUS + Utils.PLAY;
					gui.l_status.setText(status);

				}

				if (noskipcycles > 0) {
					noskipcycles--;
				} else {
					gui.sl_currentSong.setValue((int) Utils.map(in.available(), 0, trckLegit, 391, 0));
					timeline = Utils
							.getTime(trackSizeInmicros - Utils.map(in.available(), 0, trckLegit, 0, trackSizeInmicros))
							+ "|" + duration;
					gui.l_timer.setText(timeline);
				}
			}

			isEnded = true;
			forAllClients(createPack());
			timeline = duration + "|" + duration;
			gui.sl_currentSong.resetAll(false);
			SMS.say("Clip ended");
			Output.drain();
			Output.stop();
			Output.close();
			din.close();
			in.close();

		} catch (Exception e) {
			gui.l_status.setText(Utils.STATUS + MainClass.lang.getLocale("error::readError"));
			Utils.sleep(400);
			e.printStackTrace();
		}
		gui.sl_currentSong.setEnabled(false);
	}

	////////////////////// Controll ///////////////////////////////////

	public static void mute() {
		if (!isMute) {
			gui.s_volume.setEnabled(false);
			lastvolume = gui.s_volume.getValue();
			gui.s_volume.setValue(0);
			gui.b_mute.setIcon(MainClass.rl.getImage("mute"));
			isMute = true;
		} else {
			gui.s_volume.setEnabled(true);
			gui.s_volume.setValue(lastvolume);
			gui.b_mute.setIcon(MainClass.rl.getImage("unmute"));
			isMute = false;
		}
	}

	public static void pause() {
		if (Server.isPaused) {
			gui.b_play.setIcon(MainClass.rl.getImage("pause"));
			if (!isMute) {
				VolumeController.unmute();
			}
			Server.isPaused = false;
		} else {
			gui.b_play.setIcon(MainClass.rl.getImage("play"));

			VolumeController.mute();
			Server.isPaused = true;
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

	public static void SetTrackPos(int x) {
		gui.sl_currentSong.resetAll(false);
		HowManySkip = (long) Utils.map(x, 0, 391, 0, trckLegit);
		noskipcycles = 1;
		gui.sl_currentSong.setValue((int) Utils.map(HowManySkip, trckLegit, 0, 391, 0));

	}

	public static String GetPresetPoss(int x) {
		return Utils.getTime(
				trackSizeInmicros - Utils.map(Utils.map(x, 391, 0, 0, trckLegit), 0, trckLegit, 0, trackSizeInmicros));

	}

	/////////////////// Net////////////////////

	public PacketTrack createPack() {
		return new PacketTrack(data, num, getNetCode(), ClientListener.ÑlientsConnections.size(), sampler,
				gui.sl_currentSong.getValue(), buildString());
	}

	public ByteBuffer createDataPack(PacketTrack obj) throws IOException {
		sendbuffer.rewind();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream ois = new ObjectOutputStream(bos);
		ois.writeObject(obj);
		bos.close();
		ois.close();
		sendbuffer.put(bos.toByteArray());
		sendbuffer.position(SIZE);
		sendbuffer.flip();

		return sendbuffer;
	}

	public static byte getNetCode() {

		if (isEnded) {
			return NetCodes.ENDED;
		} else if (isSkip) {
			return NetCodes.SKIPPED;
		} else if (isPaused) {
			return NetCodes.PAUSED;
		} else if (isPosCha) {
			return NetCodes.POS_CHANGED;
		} else {
			return -1;
		}

	}

	public String buildString() {
		strigBuffer.setLength(0);
		strigBuffer.append(title + "\n");
		strigBuffer.append(status + "\n");
		strigBuffer.append(timeline + "\n");
		strigBuffer.append(Utils.VERSION);
		return strigBuffer.toString();
	}

	public void proccesCommand(byte com) {
		switch (com) {
		case NetCodes.TS_SKIP:
			isSkip = true;
			break;
		case NetCodes.TS_REPLAY:
			isReplaed = true;
			isSkip = true;
			break;
		default:
			break;
		}

	}

	public void forAllClients(PacketTrack pac) {
		for (ClientConnection k : ClientListener.ÑlientsConnections) {
			try {
				k.connection.write(createDataPack(createPack()));

				if (!inputCommandBuffer.hasRemaining()) {
					inputCommandBuffer.flip();
					proccesCommand(inputCommandBuffer.get());
					inputCommandBuffer.rewind();
				} else {
					k.connection.read(inputCommandBuffer);
				}

				k.connection.socket().getOutputStream().flush();

			} catch (Exception e) {
				e.printStackTrace();
				ClientListener.ÑlientsConnections.remove(k);
				try {
					k.connection.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}