package com.SHGroup.mitm.networking.packets;

public enum PacketType {
	ARP("ARP", ARPPacket.class);
	private final String name;
	private final Class<? extends AbstractPacket> real;
	private AbstractPacket packet;
	private PacketType(String name, Class<? extends AbstractPacket> real) {
		this.name = name;
		this.real = real;
	}
	
	public String getName() {
		return name;
	}
	
	public Class<? extends AbstractPacket> getRealType(){
		return real;
	}
	
	public byte[] getEthernetType() {
		if(packet == null){
			try {
				packet = real.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(packet != null) {
			return packet.getEthernetType();
		}
		return new byte[2];
	}
}
