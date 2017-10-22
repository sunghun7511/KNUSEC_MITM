package com.SHGroup.mitm.command;

public abstract class CommandExecutor {
	private String name;
	private String command;
	
	public final String getName() {
		return name;
	}
	
	public final String getCommand() {
		return command;
	}
	
	public abstract boolean execute(String[] args);
}
