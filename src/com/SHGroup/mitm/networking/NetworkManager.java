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
	
	public ArrayList<String> getNetworkDevices() {
		ArrayList<PcapIf> allDevices = new ArrayList<>();
		StringBuilder err = new StringBuilder();
		
		int res = Pcap.findAllDevs(allDevices, err);
		if (res == Pcap.NOT_OK || allDevices.isEmpty()) {
			Main.gui.appendLog("Cannot loads network devices..");
			Main.gui.appendLog("Error : " + err.toString());
			return lastDevices;
		}
		lastDevices = new ArrayList<>();

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
		if(!arpspoof.isSet()) {
			System.out.println("INIT ARP");
			arpspoof.initArpSpoofing();
			System.out.println("INIT ARP FINISH");
		}
		ArrayList<String> devices = new ArrayList<>();
		
		return devices;
	}

	public boolean selectNetworkCard(int index) {
		if (index < 0 || index >= allDevices.size()) {
			Main.gui.appendLog("Error while select network card : " + index + " is not invalid index.");
			return false;
		}
		if (pcap != null) {
			Main.gui.appendLog("Already another device is opened.");
			Main.gui.appendLog("Close device '" + Utils.getName(device) + "'.");
			pcap.close();
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
		if(abp instanceof ARPPacket) {
			arpspoof.responseARPReply((ARPPacket) abp);
		}
	}
}