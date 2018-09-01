package ru.rinpolz.streamplayer.network;

public final class NetCodes {

	// Only Final is fine!
	public static final byte PAUSED = 1;
	public static final byte SKIPPED = 2;
	public static final byte POS_CHANGED = 3;
	public static final byte ENDED = 4;
	
	//For sending to server
	public static final byte TS_SKIP = 10;
	public static final byte TS_REPLAY = 11;
	
	

	private NetCodes() {
	}

}
