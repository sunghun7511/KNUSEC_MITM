package com.SHGroup.mitm.command;

public class InvalidTokenizeException extends Exception{
	private static final long serialVersionUID = 471878045310500258L;
	private final String message;
	public InvalidTokenizeException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
