package com.SHGroup.mitm.command.commands;

import com.SHGroup.mitm.Main;
import com.SHGroup.mitm.command.CommandExecutor;

public class CommandExit extends CommandExecutor {
	@Override
	public boolean execute(String[] args) {
		if (args.length == 1) {
			Main.exit();
		} else if (args.length > 2) {
			int i = Integer.parseInt(args[1]);
			if (i == 0) {
				double d = Double.parseDouble(args[2]);
				Main.gui.getController().getGraphManager().vp.getCamera().setViewPercent(d);
			} else if (i == 1) {
				Main.gui.getController().getGraphManager().mainGraph.addNode(args[2]);
			} else if (i == 2) {
				Main.gui.getController().getGraphManager().mainGraph.addEdge(args[2], args[3], args[4]);
			}
		} else if (args.length == 2) {
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
