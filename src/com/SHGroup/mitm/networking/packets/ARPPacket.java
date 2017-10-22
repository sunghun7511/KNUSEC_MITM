package com.SHGroup.mitm.networking.packets;

import com.SHGroup.mitm.Utils;

public class ARPPacket extends AbstractPacket {

	private byte[] destMac = new byte[6];
	private byte[] srcMac = new byte[6];

	private byte[] ethernetType = new byte[] { 0x08, 0x06 }; // ARP
	private byte[] hardwareType = new byte[] { 0x00, 0x01 }; // Ethernet
	private byte[] protocolType = new byte[] { 0x08, 0x00 }; // IPv4
	private byte hardwareSize = 0x06; // Mac Size
	private byte protocolSize = 0x04; // IP Size

	private byte[] opcode = new byte[2]; // Reply or Request

	private byte[] senderMac = new byte[6];
	private byte[] senderIP = new byte[4];

	private byte[] targetMac = new byte[6];
	private byte[] targetIP = new byte[4];

	@Override
	public byte[] generatePacket() {
		int index = 0;
		byte[] packet = new byte[42];

		index += Utils.copy(destMac, packet, index);
		index += Utils.copy(srcMac, packet, index);

		index += Utils.copy(ethernetType, packet, index);
		index += Utils.copy(hardwareType, packet, index);
		index += Utils.copy(protocolType, packet, index);

		packet[18] = hardwareSize;
		packet[19] = protocolSize;
		index += 2;

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
