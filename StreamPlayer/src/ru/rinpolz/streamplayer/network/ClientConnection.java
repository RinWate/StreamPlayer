package ru.rinpolz.streamplayer.network;

import java.nio.channels.SocketChannel;

import ru.rinpolz.streamplayer.utill.Utils;

public class ClientConnection {

	public SocketChannel connection;

	public ClientConnection(SocketChannel s) {

		
		
		try {
			this.connection = s;
			connection.configureBlocking(false);
		
			
			//TODO loop
			while (!connection.finishConnect()) {
				Utils.sleep(100);
			}

			if (ClientListener.ÑlientsConnections.size() > 5) {
				connection.close();
			} else {
				ClientListener.ÑlientsConnections.add(this);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
