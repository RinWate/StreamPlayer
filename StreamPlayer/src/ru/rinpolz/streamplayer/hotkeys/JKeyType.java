package ru.rinpolz.streamplayer.hotkeys;

import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.swing.JOptionPane;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

import ru.rinpolz.streamplayer.mainlogic.Client;
import ru.rinpolz.streamplayer.mainlogic.MainClass;
import ru.rinpolz.streamplayer.mainlogic.Server;
import ru.rinpolz.streamplayer.mainlogic.Settings;
import ru.rinpolz.streamplayer.network.ClientInputReader;
import ru.rinpolz.streamplayer.network.NetCodes;

public class JKeyType implements HotkeyListener {

	String homeDir = System.getProperty("user.home");

	public JKeyType() {

		if (!new File(homeDir + "//.RadioSettings//KeyListener.dll").exists()
				|| !new File(homeDir + "//.RadioSettings//KeyListener32.dll").exists()) {

			System.out.println("DDls not found unpacking...");

			new File(homeDir + "//.RadioSettings").mkdir();

			unpackDLLs("KeyListener.dll");
			unpackDLLs("KeyListener32.dll");

		}

		try {
			JIntellitype.setLibraryLocation(homeDir + "//.RadioSettings//KeyListener.dll");
			JIntellitype.getInstance();
		} catch (Exception e) {
			try {
				JIntellitype.setLibraryLocation(homeDir + "//.RadioSettings//KeyListener32.dll");
				JIntellitype.getInstance();
				e.printStackTrace();
			} catch (Exception e2) {

				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "The library does not support the system", "Error",
						JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}

		}

		initKeys();

	}

	public void initKeys() {

		try {

			JIntellitype.getInstance().removeHotKeyListener(this);

			JIntellitype.getInstance().unregisterHotKey(0);
			JIntellitype.getInstance().unregisterHotKey(1);
			JIntellitype.getInstance().unregisterHotKey(2);
			JIntellitype.getInstance().unregisterHotKey(3);
			JIntellitype.getInstance().unregisterHotKey(4);
			JIntellitype.getInstance().unregisterHotKey(5);

			JIntellitype.getInstance().registerHotKey(0, Settings.keys[0], Settings.keys[1]);
			JIntellitype.getInstance().registerHotKey(1, Settings.keys[2], Settings.keys[3]);
			JIntellitype.getInstance().registerHotKey(2, Settings.keys[4], Settings.keys[5]);
			JIntellitype.getInstance().registerHotKey(4, Settings.keys[6], Settings.keys[7]);
			JIntellitype.getInstance().registerHotKey(3, Settings.keys[8], Settings.keys[9]);

			///
			JIntellitype.getInstance().registerHotKey(5, Settings.keys[10], Settings.keys[11]);
			JIntellitype.getInstance().addHotKeyListener(this);

		} catch (Exception e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);

		}

	}

	@Override
	public void onHotKey(int i) {
		switch (i) {
		case 0:

			if (MainClass.isRemote) {

				System.out.println("Skipping");
				Server.gui.sl_currentSong.resetAll(false);
				Server.isSkip = true;
			} else {
				ClientInputReader.command = NetCodes.TS_SKIP;
			}

			break;
		case 1:
			System.out.println("Pause");
			Server.pause();
			break;
		case 2:
			System.out.println("Volume Up");
			if (MainClass.isRemote) {
				Server.moveVolume(true);
			} else {
				Client.moveVolume(true);
			}
			break;
		case 3:
			System.out.println("Volume Down");
			if (MainClass.isRemote) {
				Server.moveVolume(false);

			} else {
				Client.moveVolume(false);
			}

			break;
		case 4:
			System.out.println("Mute");
			if (MainClass.isRemote) {
				Server.mute();
			} else {
				Client.mute();
			}
			break;

		case 5:

			if (MainClass.isRemote) {
				Server.gui.sl_currentSong.resetAll(false);
				Server.isReplaed = true;
				Server.isSkip = true;
			} else {
				ClientInputReader.command = NetCodes.TS_REPLAY;
			}

			break;

		}

	}

	public void unpackDLLs(String ddlName) {
		FileOutputStream Out = null;
		try {

			System.out.println("Копируем ");
			InputStream in = getClass().getClassLoader().getResourceAsStream(ddlName);
			BufferedInputStream bis = new BufferedInputStream(in);
			Out = new FileOutputStream(homeDir + "//.RadioSettings//" + ddlName);
			byte[] bar = new byte[1024];
			int count = 0;
			while (count != -1) {
				Out.write(bar, 0, count);
				count = bis.read(bar);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
