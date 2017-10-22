package com.SHGroup.mitm;

import org.jnetpcap.PcapIf;

public class Utils {
	public final static String getName(PcapIf device) {
		return getName(device, " ¦¡ ");
	}

	public final static String getName(PcapIf device, String split) {
		return (device.getDescription() != null ? device.getDescription() + split : "") + device.getName();
	}

	public final static String bytesToString(byte[] arr) {
		return bytesToString(arr, 16);
	}

	public final static String bytesToString(byte[] arr, int linefeed) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < arr.length;) {
			builder.append(String.format("%02x", arr[i] & 0xff)).append(++i % linefeed == 0 ? "\n" : " ");
		}
		return builder.toString();
	}

	public final static String IPToString(byte[] ip) {
		if (ip.length != 4) {
			return "";
		}
		return new StringBuilder().append(Integer.toString(ip[0] & 0xff)).append(".")
				.append(Integer.toString(ip[1] & 0xff)).append(".").append(Integer.toString(ip[2] & 0xff)).append(".")
				.append(Integer.toString(ip[3] & 0xff)).toString();
	}

	public final static int copy(byte[] src, byte[] dest) {
		if (src.length > dest.length) {
			return 0;
		}
		return copy(src, 0, dest, 0, dest.length);
	}
	
	public final static int copy(byte[] src, int srcstart, byte[] dest, int deststart) {
		if (deststart < 0 || srcstart < 0) {
			return 0;
		}
		return copy(src, srcstart, dest, deststart, Math.min(src.length - srcstart, dest.length - deststart));
	}
	
	public final static int copy(byte[] src, byte[] dest, int deststart) {
		if (deststart < 0) {
			return 0;
		}
		if (dest.length - deststart < src.length) {
			return 0;
		}
		return copy(src, 0, dest, deststart, src.length);
	}

	public final static int copy(byte[] src, byte[] dest, int deststart, int amount) {
		if (deststart < 0 || amount < 1) {
			return 0;
		}
		if (deststart + amount > dest.length) {
			return 0;
		}
		if (amount > src.length) {
			return 0;
		}
		return copy(src, 0, dest, deststart, amount);
	}

	public final static int copy(byte[] src, int srcstart, byte[] dest, int deststart, int amount) {
		if (deststart < 0 || srcstart < 0 || amount < 1 || amount > dest.length - deststart
				|| srcstart + amount > src.length) {
			return 0;
		}
		for (int i = 0; i < amount; i++) {
			dest[deststart + i] = src[srcstart + i];
		}
		return amount;
	}
}
