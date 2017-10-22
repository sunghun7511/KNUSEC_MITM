package com.SHGroup.mitm.networking;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.JRegistry;
import org.jnetpcap.packet.PcapPacket;

import com.SHGroup.mitm.Main;
import com.SHGroup.mitm.networking.packets.AbstractPacket;
import com.SHGroup.mitm.networking.packets.PacketType;

public class PacketCaptureThread extends Thread{
	
	private boolean exit = false;
	
	@Override
	public void run() {
		while(!exit) {
			try {
				if(Main.network.getPcap() != null) {
					PcapHeader header = new PcapHeader(JMemory.POINTER);
					JBuffer buf = new JBuffer(JMemory.POINTER);
					
					int id = JRegistry.mapDLTToId(Main.network.getPcap().datalink());
					
					while(Main.network.getPcap().nextEx(header, buf)
							!= Pcap.NEXT_EX_NOT_OK) {
						PcapPacket packet = new PcapPacket(header, buf);
						packet.scan(id);
						
						for(PacketType type : PacketType.values()) {
							if(type.getEthernetType()[0] == packet.getByte(12)
									&& type.getEthernetType()[1] == packet.getByte(13)) {
								try {
									byte[] bytes = new byte[packet.size()];
									for(int i = 0 ; i < packet.size() ; i ++) {
										bytes[i] = packet.getByte(i);
									}
									AbstractPacket abp = type.getRealType().getConstructor(byte[].class).newInstance(bytes);
									Main.network.dispatchPacket(abp);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
