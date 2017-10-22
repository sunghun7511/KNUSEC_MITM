package com.SHGroup.mitm.networking.packets;

public class InvalidPacketException extends Exception{
	private static final long serialVersionUID = 3445479196613128207L;

	private final String message;
	public InvalidPacketException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
