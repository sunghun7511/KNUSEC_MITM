package com.SHGroup.mitm.networking.packets;

import com.SHGroup.mitm.Utils;

public class AbstractPacket {
	protected byte[] destMac = new byte[6];
	protected byte[] srcMac = new byte[6];

	protected byte[] ethernetType = new byte[2];
	
	public AbstractPacket(byte[] packet) throws InvalidPacketException {
		if(packet.length < 14) {
			throw new InvalidPacketException("need at least 14 bytes.");
		}
		int index = 0;
		
		index += Utils.copy(packet, index, destMac, 0);
		index += Utils.copy(packet, index, srcMac, 0);
		index += Utils.copy(packet, index, ethernetType, 0);
	}
	
	public AbstractPacket(byte ethernetType1, byte ethernetType2) {
		this.ethernetType = new byte[] {ethernetType1, ethernetType2};
	}
	
	public byte[] generatePacket() {
		int index = 0;
		byte[] packet = new byte[14];

		index += Utils.copy(destMac, packet, index);
		index += Utils.copy(srcMac, packet, index);
		index += Utils.copy(ethernetType, packet, index);
		
		return packet;
	}

	public byte[] getEthernetType() {
		return ethernetType;
	}
}
