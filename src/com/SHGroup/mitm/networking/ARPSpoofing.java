package com.SHGroup.mitm.networking;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.jnetpcap.Pcap;

import com.SHGroup.mitm.Main;
import com.SHGroup.mitm.Utils;
import com.SHGroup.mitm.networking.packets.ARPPacket;

public class ARPSpoofing {
	private byte[] localIP = null;
	private byte[] localMac = null;

	private byte[] routerIP = null;
	private byte[] routerMac = null;

	private boolean isSet = false;

	public ARPSpoofing() {
	}

	public void initArpSpoofing() {
		try {
			localMac = Main.network.getDevice().getHardwareAddress();

			// ip auto detect..
			// hard coding xD
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) e.nextElement();
				if (n.getHardwareAddress() != null
						&& Utils.bytesToString(n.getHardwareAddress()).equals(Utils.bytesToString(localMac))) {
					Enumeration<InetAddress> ee = n.getInetAddresses();
					while (ee.hasMoreElements()) {
						InetAddress inet = (InetAddress) ee.nextElement();
						if (inet instanceof Inet4Address) {
							System.out.println(inet.getHostAddress());
							Main.gui.appendLog("IP auto detect : " + inet.getHostAddress());
							localIP = inet.getAddress();
						}
					}
				}
			}
			isSet = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void sendARPRequest() {
		try {

			// hard coding broadcasting.. haha
			// some hard coding makes speed faster

//			for (byte i = 0; i < 256; i++) {
//				if (i == localIP[3]) {
//					continue;
//				}
//				byte[] target = new byte[] { localIP[0], localIP[1], localIP[2], i };
//				ARPPacket arp = ARPPacket.generateARPRequstPacket(localMac, localIP, target);
//
//				int isOk = Main.network.getPcap().sendPacket(arp.generatePacket());
//				if (isOk != Pcap.OK) {
//					Main.gui.appendLog("Error on broadcasting..");
//					Main.gui.appendLog(" Cause : " + Main.network.getPcap().getErr());
//				}
//				System.out.println(Utils.IPToString(target));
//				Main.gui.appendLog("Send arp request to " + Utils.IPToString(target) + "..");
//			}
			final byte ff = (byte) 0xff;
			byte[] target = new byte[] {ff, ff, ff, ff};
			ARPPacket arp = ARPPacket.generateARPRequstPacket(localMac, localIP, target);

			int isOk = Main.network.getPcap().sendPacket(arp.generatePacket());
			if (isOk != Pcap.OK) {
				Main.gui.appendLog("Error on broadcasting..");
				Main.gui.appendLog(" Cause : " + Main.network.getPcap().getErr());
			}
			System.out.println(Utils.IPToString(target));
			Main.gui.appendLog("Send arp request to " + Utils.IPToString(target) + "..");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean isSet() {
		return isSet;
	}
}
