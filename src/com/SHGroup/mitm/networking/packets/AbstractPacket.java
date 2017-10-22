package com.SHGroup.mitm.networking.packets;

import com.SHGroup.mitm.Utils;

public abstract class AbstractPacket {
	protected byte[] destMac = new byte[6];
	protected byte[] srcMac = new byte[6];

	protected byte[] ethernetType = new byte[2];

	public AbstractPacket(byte[] packet) throws InvalidPacketException {
		if(packet.length < 14) {
			throw new InvalidPacketException("need at least 14 bytes.");
		}
		Utils.copy(packet, destMac, 0, 6);
		Utils.copy(packet, srcMac, 6, 6);
		Utils.copy(packet, ethernetType, 12, 2);
	}
	
	public AbstractPacket(byte ethernetType1, byte ethernetType2) {
		this.ethernetType = new byte[] {ethernetType1, ethernetType2};
	}
	
	public AbstractPacket() {}
	
	public byte[] generatePacket() {
		int index = 0;
		byte[] packet = new byte[14];

		index += Utils.copy(destMac, packet, index);
		index += Utils.copy(srcMac, packet, index);
		
		index += Utils.copy(ethernetType, packet, index);
		
		return packet;
	}
}
