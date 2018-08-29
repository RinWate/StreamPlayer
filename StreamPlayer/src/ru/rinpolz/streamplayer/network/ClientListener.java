package ru.rinpolz.streamplayer.network;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JOptionPane;

import ru.rinpolz.streamplayer.mainlogic.MainClass;
import ru.rinpolz.streamplayer.mainlogic.Server;

public class ClientListener extends Thread {
	public static CopyOnWriteArrayList<ClientConnection> ÑlientsConnections = new CopyOnWriteArrayList<ClientConnection>();
	static boolean checed;

	public void run() {
		try {

			ServerSocketChannel ss = ServerSocketChannel.open();
			ss.socket().bind(new InetSocketAddress(MainClass.port));

			checed = true;
			Server.isOpen = true;

			while (true) {
				Thread.sleep(300);
				new ClientConnection(ss.accept());
			}

		} catch (Exception e) {
			e.printStackTrace();
			checed = true;
			Server.isOpen = false;
			JOptionPane.showMessageDialog(MainClass.login, "Failed to start the server! Failed to bind to port!",
					"Error", JOptionPane.ERROR_MESSAGE);

			System.exit(0);

		}
	}
}