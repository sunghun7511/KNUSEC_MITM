package com.SHGroup.mitm.networking;

import java.util.ArrayList;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

import com.SHGroup.mitm.Main;
import com.SHGroup.mitm.Utils;
import com.SHGroup.mitm.networking.packets.ARPPacket;
import com.SHGroup.mitm.networking.packets.AbstractPacket;

public class NetworkManager {
	private boolean isInit = false;

	private final ArrayList<Device> devices = new ArrayList<>();

	private Pcap pcap;
	private PcapIf device;

	private ArrayList<PcapIf> allDevices = null;
	private ArrayList<String> lastDevices = null;

	private ARPSpoofing arpspoof;

	private NetworkThread nTherad;
	private PacketCaptureThread pCaptureThread;

	public void initialize() {
		if (isInit)
			return;
		isInit = true;

		nTherad = new NetworkThread();
		nTherad.setDaemon(true);
		nTherad.start();

		pCaptureThread = new PacketCaptureThread();
		pCaptureThread.setDaemon(true);

		arpspoof = new ARPSpoofing();
	}

	public void onExit() {

	}

	public NetworkThread getThread() {
		return nTherad;
	}

	public PcapIf getDevice() {
		return device;
	}

	public Pcap getPcap() {
		return pcap;
	}

	public int getDevicesSize() {
		return devices.size();
	}

	public Device getDeviceFromIP(byte[] ip) {
		Device d = null;
		for (Device dd : devices) {
			if (Utils.bytesToString(dd.getIP()).equals(Utils.bytesToString(ip))) {
				d = dd;
				break;
			}
		}
		return d;
	}

	public Device getDeviceFromMac(byte[] mac) {
		Device d = null;
		for (Device dd : devices) {
			if (Utils.bytesToString(dd.getMac()).equals(Utils.bytesToString(mac))) {
				d = dd;
				break;
			}
		}
		return d;
	}

	public Device[] getDevices() {
		return devices.toArray(new Device[] {});
	}

	public Device getDevice(int index) {
		if (index < 0 || index >= devices.size()) {
			return null;
		}
		return devices.get(index);
	}

	public int addDevice(Device device) {
		if(getDeviceFromMac(device.getMac()) != null) {
			return -1;
		}
		devices.add(device);
		return devices.size() - 1;
	}

	public ArrayList<String> getNetworkDevices() {
		ArrayList<PcapIf> allDevices = new ArrayList<>();
		StringBuilder err = new StringBuilder();

		System.out.println("load_All");
		int res = Pcap.findAllDevs(allDevices, err);
		if (res == Pcap.NOT_OK || allDevices.isEmpty()) {
			Main.gui.appendLog("Cannot loads network devices..");
			Main.gui.appendLog("Error : " + err.toString());
			return lastDevices;
		}
		lastDevices = new ArrayList<>();
		System.out.println("load_All");
		Main.gui.appendLog("Find " + allDevices.size() + " devices.");
		for (int i = 0; i < allDevices.size(); i++) {
			PcapIf pci = allDevices.get(i);

			String name = Utils.getName(pci, "\n	");

			Main.gui.appendLog(" [ " + Integer.toString(i + 1) + " ] " + Utils.getName(pci));
			lastDevices.add(name);
		}

		this.allDevices = allDevices;
		return lastDevices;
	}

	public ArrayList<String> getARPTargetDevices() {
		if (!arpspoof.isSet()) {
			arpspoof.initArpSpoofing();
		}
		ArrayList<String> devices = new ArrayList<>();
		
		Device rout = arpspoof.getRouter();
		devices.add(rout == null ? "게이트웨이 : 없음" : "게이트웨이 : " + rout.getNickName());
		
		for (Device d : this.devices) {
			devices.add(d.getNickName());
		}

		return devices;
	}
	
	public void closeNetworkCard() {
		Main.gui.appendLog("Close device '" + Utils.getName(device) + "'.");
		pcap.close();
		pcap = null;
	}

	public boolean selectNetworkCard(int index) {
		if (index < 0 || index >= allDevices.size()) {
			Main.gui.appendLog("Error while select network card : " + index + " is not invalid index.");
			return false;
		}
		if (pcap != null) {
			System.out.println("blabla");
			Main.gui.appendLog("Already another device is opened.");
			closeNetworkCard();
		}
		StringBuilder err = new StringBuilder();
		
		device = allDevices.get(index);
		pcap = Pcap.openLive(device.getName(), 64 * 1024, Pcap.MODE_PROMISCUOUS, 1000, err);

		if (pcap == null) {
			Main.gui.appendLog("Error while openning network card");
			Main.gui.appendLog(" Cause : " + err.toString());
			device = null;
			return false;
		}

		pCaptureThread.start();

		Main.gui.appendLog("Success select network card! : " + Utils.getName(device));
		return true;
	}

	public void dispatchPacket(AbstractPacket abp) {
		if (abp instanceof ARPPacket) {
			arpspoof.responseARPReply((ARPPacket) abp);
		}
	}
}