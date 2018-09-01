package ru.rinpolz.streamplayer.hotkeys;

import javax.swing.KeyStroke;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

import ru.rinpolz.streamplayer.mainlogic.Client;
import ru.rinpolz.streamplayer.mainlogic.MainClass;
import ru.rinpolz.streamplayer.mainlogic.Server;
import ru.rinpolz.streamplayer.mainlogic.Settings;
import ru.rinpolz.streamplayer.network.ClientInputReader;
import ru.rinpolz.streamplayer.network.NetCodes;

public class JKeyType {

	Provider provider = Provider.getCurrentProvider(false);

	public JKeyType() {
		initKeys();
	}

	public void initKeys() {
		provider.reset();

		regHotKey(Settings.keys[0], Settings.keys[1], new HotKeyListener() {
			// Skip
			@Override
			public void onHotKey(HotKey hotKey) {
				if (MainClass.isServer) {
					System.out.println("Skipping");
					Server.gui.sl_currentSong.resetAll(false);
					Server.isSkip = true;

				} else {
					ClientInputReader.command = NetCodes.TS_SKIP;
				}
			}
		});

		regHotKey(Settings.keys[2], Settings.keys[3], new HotKeyListener() {

			@Override
			public void onHotKey(HotKey hotKey) {
				System.out.println("Pause");
				Server.pause();

			}
		});
		regHotKey(Settings.keys[4], Settings.keys[5], new HotKeyListener() {

			@Override
			public void onHotKey(HotKey hotKey) {

				System.out.println("Volume Up");
				if (MainClass.isServer) {
					Server.moveVolume(true);
				} else {
					Client.moveVolume(true);
				}
			}
		});
		regHotKey(Settings.keys[6], Settings.keys[7], new HotKeyListener() {

			@Override
			public void onHotKey(HotKey hotKey) {
				System.out.println("Mute");
				if (MainClass.isServer) {
					Server.mute();
				} else {
					Client.mute();
				}
			}
		});
		regHotKey(Settings.keys[8], Settings.keys[9], new HotKeyListener() {

			@Override
			public void onHotKey(HotKey hotKey) {
				System.out.println("Volume Down");
				if (MainClass.isServer) {
					Server.moveVolume(false);

				} else {
					Client.moveVolume(false);
				}

			}
		});
		regHotKey(Settings.keys[10], Settings.keys[11], new HotKeyListener() {

			@Override
			public void onHotKey(HotKey hotKey) {
				System.out.println("Replay");
				if (MainClass.isServer) {
					Server.gui.sl_currentSong.resetAll(false);
					Server.isReplaed = true;
					Server.isSkip = true;
				} else {
					ClientInputReader.command = NetCodes.TS_REPLAY;
				}

			}
		});

	}

	public void regHotKey(int a, int k, HotKeyListener l) {
		// System.out.println(KeyStroke.getKeyStroke(k, STHotKey.GetHostKey(a), false));
		provider.register(KeyStroke.getKeyStroke(k, STHotKey.GetHostKey(a), false), l);
	}

}
