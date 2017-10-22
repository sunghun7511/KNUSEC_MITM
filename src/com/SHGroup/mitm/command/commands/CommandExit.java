package com.SHGroup.mitm.command.commands;

import com.SHGroup.mitm.Main;
import com.SHGroup.mitm.command.CommandExecutor;

public class CommandExit extends CommandExecutor {
	@Override
	public boolean execute(String[] args) {
		if (args.length == 1) {
			Main.exit();
		} else {
			int exit_code = 0;
			try {
				exit_code = Integer.parseInt(args[1]);
			} catch (Exception ex) {
				exit_code = 0;
			}
			Main.exit(exit_code);
		}
		return true;
	}

}
