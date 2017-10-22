package com.SHGroup.mitm.networking;

import java.util.HashMap;

import com.SHGroup.mitm.Utils;
import com.SHGroup.mitm.networking.packets.AbstractPacket;

public class Device {
	private int in_temp = 0, out_temp = 0;
	private final HashMap<Integer, AbstractPacket> transport_in = new HashMap<>();
	private final HashMap<Integer, AbstractPacket> transport_out = new HashMap<>();
	private byte[] mac = new byte[6];
	private byte[] ip = new byte[4];
	private String nickname;

	public Device(byte[] mac, byte[] ip) {
		this.ip = ip;
		this.mac = mac;
	}

	public String setNickName(String name) {
		return name == null ? getNickName() : (nickname = name);
	}

	public String getNickName() {
		if (nickname == null) {
			return Utils.IPToString(ip) + "(" + Utils.bytesToString(mac) + ")";
		}
		return nickname;
	}

	public byte[] getIP() {
		return ip;
	}

	public byte[] getMac() {
		return mac;
	}

	public int receivePacket(AbstractPacket packet) {
		transport_in.put(in_temp, packet);
		return in_temp++;
	}

	public int sendPacket(AbstractPacket packet) {
		transport_out.put(out_temp, packet);
		return out_temp;
	}

}
