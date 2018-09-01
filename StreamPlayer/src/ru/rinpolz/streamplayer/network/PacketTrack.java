package ru.rinpolz.streamplayer.network;

import java.io.Serializable;

public class PacketTrack implements Serializable {
	private static final long serialVersionUID = 1L;
	public float samplerate;

	public String stringData = "";

	public byte[] data;
	public byte netCode = -1;
	
	public int leg;
	public int num_of_clients = 0;
	public int progress;

	public PacketTrack(byte[] b, int leg, byte NetCode, int clients, float smpr, int prog, String datas) {
		this.stringData = datas;
		this.netCode = NetCode;
		this.progress = prog;
		this.samplerate = smpr;
		this.data = b;
		this.leg = leg;
		this.num_of_clients = clients;
	}
}