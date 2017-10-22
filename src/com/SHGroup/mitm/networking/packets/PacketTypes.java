package com.SHGroup.mitm.networking.packets;

public enum PacketTypes {
	ARP("ARP", ARPPacket.class);
	private final String name;
	private final Class<? extends AbstractPacket> real;
	private PacketTypes(String name, Class<? extends AbstractPacket> real) {
		this.name = name;
		this.real = real;
	}
	
	public String getName() {
		return name;
	}
	
	public Class<? extends AbstractPacket> getRealType(){
		return real;
	}
}
