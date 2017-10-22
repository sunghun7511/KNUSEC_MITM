package com.SHGroup.mitm.networking.packets;

import com.SHGroup.mitm.Utils;

public final class ARPPacket extends AbstractPacket {
	public ARPPacket(byte[] packet) throws InvalidPacketException {
		super(packet);
		if(packet.length < 42) {
			throw new InvalidPacketException("need at least 42 bytes.");
		}
		int index = 14;
		index += Utils.copy(packet, index, hardwareType, 0);
		index += Utils.copy(packet, index, protocolType, 0);
		
		hardwareSize = packet[index++];
		protocolSize = packet[index++];
		
		index += Utils.copy(packet, index, opcode, 0);

		index += Utils.copy(packet, index, senderMac, 0);
		index += Utils.copy(packet, index, senderIP, 0);

		index += Utils.copy(packet, index, targetMac, 0);
		index += Utils.copy(packet, index, targetIP, 0);
	}
	
	public ARPPacket() {
		super((byte)0x08, (byte)0x06);
	}

	private byte[] hardwareType = new byte[] { 0x00, 0x01 }; // Ethernet

	private byte[] protocolType = new byte[] { 0x08, 0x00 }; // IPv4
	
	private byte hardwareSize = 0x06; // Hardware Size(MAC)
	private byte protocolSize = 0x04; // Protocol Size(IP)
	
	private byte[] opcode = new byte[2]; // Reply or Request

	private byte[] senderMac = new byte[6];
	private byte[] senderIP = new byte[4];

	private byte[] targetMac = new byte[6];
	private byte[] targetIP = new byte[4];

	public final byte[] getHardwareType() {
		return hardwareType;
	}

	public final byte[] getProtocolType() {
		return protocolType;
	}

	public final byte getHardwareSize() {
		return hardwareSize;
	}

	public final byte getProtocolSize() {
		return protocolSize;
	}

	public final byte[] getOpcode() {
		return opcode;
	}

	public final byte[] getSenderMac() {
		return senderMac;
	}

	public final byte[] getSenderIP() {
		return senderIP;
	}

	public final byte[] getTargetMac() {
		return targetMac;
	}

	public final byte[] getTargetIP() {
		return targetIP;
	}

	@Override
	public byte[] generatePacket() {
		int index = 0;
		byte[] packet = new byte[42];
		
		index += Utils.copy(super.generatePacket(), packet, index);
		
		index += Utils.copy(hardwareType, packet, index);
		index += Utils.copy(protocolType, packet, index);

		packet[index++] = hardwareSize;
		packet[index++] = protocolSize;
		
		index += Utils.copy(opcode, packet, index);
		
		
		index += Utils.copy(senderMac, packet, index);
		index += Utils.copy(senderIP, packet, index);
		
		index += Utils.copy(targetMac, packet, index);
		index += Utils.copy(targetIP, packet, index);
		
		return packet;
	}

	public static ARPPacket generateARPRequstPacket(byte[] srcMac, byte[] senderIP, byte[] targetIP) {
		ARPPacket arpPacket = new ARPPacket();
		final byte ff = (byte) 0xff, zero = (byte) 0x00;

		arpPacket.destMac = new byte[] { ff, ff, ff, ff, ff, ff };
		arpPacket.opcode = new byte[] { (byte) 0x00, (byte) 0x01 };
		
		Utils.copy(srcMac, arpPacket.srcMac);
		
		Utils.copy(srcMac, arpPacket.senderMac);
		Utils.copy(senderIP, arpPacket.senderIP);

		arpPacket.targetMac = new byte[] { zero, zero, zero, zero, zero, zero };
		Utils.copy(targetIP, arpPacket.targetIP);

		return arpPacket;
	}

	public static ARPPacket generateARPReplyPacket(byte[] destMac, byte[] srcMac, byte[] senderMac, byte[] senderIP,
			byte[] targetMac, byte[] targetIP) {
		ARPPacket arpPacket = new ARPPacket();

		arpPacket.opcode = new byte[] { (byte) 0x00, (byte) 0x02 };

		Utils.copy(destMac, arpPacket.destMac);
		Utils.copy(srcMac, arpPacket.srcMac);

		Utils.copy(senderMac, arpPacket.senderMac);
		Utils.copy(senderIP, arpPacket.senderIP);

		Utils.copy(targetMac, arpPacket.targetMac);
		Utils.copy(targetIP, arpPacket.targetIP);

		return arpPacket;
	}
}
