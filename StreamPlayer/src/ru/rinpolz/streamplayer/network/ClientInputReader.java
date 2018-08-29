package ru.rinpolz.streamplayer.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import ru.rinpolz.streamplayer.mainlogic.Client;
import ru.rinpolz.streamplayer.mainlogic.VolumeController;

public class ClientInputReader extends Thread {

	public static int buff = 0;
	public ArrayList<PacketTrack> InputBuffer = new ArrayList<>();

	Exception Ex;
	PacketTrack packet;
	SocketChannel channel;
	boolean isError = false;

	public static boolean hasSkip = false;

	public ByteBuffer buf = ByteBuffer.allocate(Client.SIZE);
	ByteBuffer some = ByteBuffer.allocate(1);

	public ClientInputReader(SocketChannel socket) throws IOException {
		channel = socket;

		this.start();
	}

	public void run() {
		try {
			while (true) {

				/////////////////
				channel.read(buf);
				/////////////////

				buff = InputBuffer.size();

				if (!buf.hasRemaining()) {

					packet = (PacketTrack) Client.convertFromBytes(buf);

					// TODO рср
					if (packet.netCode == NetCodes.POS_CHANGED || InputBuffer.size() > 20) {
						Client.gui.sl_currentSong.resetAll(false);
						refresh();

					}

					InputBuffer.add(packet);
					buf.clear();
				}

				if (hasSkip) {
					hasSkip = false;
					some.put((byte) 10);
					some.flip();
					channel.write(some);
					some.rewind();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Ex = e;
		}
	}

	public void refresh() {
		VolumeController.current_volume = -80;
		VolumeController.mute();
		VolumeController.updVolme();

		InputBuffer.clear();
		VolumeController.unmute();
		Client.resets++;
	}

	public boolean hasDataAvailable() {

		if (InputBuffer.isEmpty()) {
			return false;
		}
		return false;

	}

	public PacketTrack getData() throws Exception {

		if (Ex != null) {
			throw Ex;
		} else {
			if (!InputBuffer.isEmpty()) {
				PacketTrack temp = InputBuffer.get(0);
				InputBuffer.remove(0);
				Client.acceped++;
				return temp;
			} else {
				return null;
			}
		}
	}

	public void stopThead() {
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}