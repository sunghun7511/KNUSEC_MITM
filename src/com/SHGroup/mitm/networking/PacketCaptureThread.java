package com.SHGroup.mitm.networking;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.JRegistry;
import org.jnetpcap.packet.PcapPacket;

import com.SHGroup.mitm.Main;

public class PacketCaptureThread extends Thread{
	
	private boolean exit = false;
	
	@Override
	public void run() {
		while(!exit) {
			if(Main.network.getPcap() != null) {
				PcapHeader header = new PcapHeader(JMemory.POINTER);
				JBuffer buf = new JBuffer(JMemory.POINTER);
				
				int id = JRegistry.mapDLTToId(Main.network.getPcap().datalink());
				
				while(Main.network.getPcap().nextEx(header, buf)
						!= Pcap.NEXT_EX_NOT_OK) {
					PcapPacket packet = new PcapPacket(header, buf);
					packet.scan(id);
					
					
				}
			}
		}
	}
}
