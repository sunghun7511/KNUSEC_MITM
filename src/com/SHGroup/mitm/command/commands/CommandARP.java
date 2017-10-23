package com.SHGroup.mitm.command.commands;

import com.SHGroup.mitm.Main;
import com.SHGroup.mitm.command.CommandExecutor;

public class CommandARP extends CommandExecutor {
	@Override
	public boolean execute(String[] args) {
		if (args.length == 1) {
			Main.gui.appendLog("");
		} else if (args.length > 1) {
			
		}
		return true;
	}
}
