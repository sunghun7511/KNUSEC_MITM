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
			builder.append(String.format("%02x", arr[0] & 0xff)).append(++i % linefeed == 0 ? "\n" : " ");
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
		return copy(src, dest, 0);
	}

	public final static int copy(byte[] src, byte[] dest, int start) {
		if (src.length + start > dest.length) {
			return 0;
		}
		return copy(src, dest, start, src.length);
	}

	public final static int copy(byte[] src, byte[] dest, int start, int amount) {
		if (src.length + start > dest.length) {
			return 0;
		}
		if (amount >= dest.length) {
			return 0;
		}
		for (int i = start; i < amount; i++) {
			dest[i] = src[i];
		}
		return amount;
	}
}
